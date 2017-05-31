package com.udevel.widgetlab;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

public class BouncingSlidingDotView extends DotView {
    private static final String TAG = BouncingSlidingDotView.class.getSimpleName();
    private Paint paint = new Paint();
    private int centerX;
    private int centerY;
    private float radius;
    private AnimatorSet animatorSet;
    private int parentLeft;
    private int parentRight;
    private int targetLeft;
    private Rect clipRect = new Rect();
    private int parentWidth;
    private int indexToParent = Integer.MIN_VALUE;
    private float bounceFraction = 1F;
    private List<Integer> targetLeftList = new ArrayList<>();
    private long ratioAnimationTotalDuration = 0;

    public BouncingSlidingDotView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(dotColor);
        canvas.getClipBounds(clipRect);
        clipRect.inset(-parentLeft + targetLeft, 0);
        canvas.save();
        canvas.translate(-parentLeft + targetLeft, 0);
        canvas.drawCircle(centerX, bounceFraction * centerY, radius, paint);
        canvas.restore();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        indexToParent = findIndexToParent((ViewGroup) getParent());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        ViewGroup parent = (ViewGroup) getParent();

        parentLeft = left + getWidth();
        parentWidth = parent.getWidth();
        parentRight = parentWidth - getWidth() - left;
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        radius = (Math.min(getWidth(), getHeight()) / 2);


        Log.d(TAG, "findIndexToParent(parent):" + findIndexToParent(parent));
        int parentPaddingStart = parent.getPaddingStart();

        Log.d(TAG, "left:" + left);
        Log.d(TAG, "getWidth():" + getWidth());
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        Log.d(TAG, "layoutParams.getMarginEnd():" + layoutParams.getMarginEnd());
        Log.d(TAG, "layoutParams.getMarginStart():" + layoutParams.getMarginStart());
        ratioAnimationTotalDuration = (long) (animationTotalDuration * (findIndexToParent(parent) + 1F) / parent.getChildCount());
    }

    @Override
    protected void init() {

    }

    @Override
    public void startDotAnimation() {
        stopDotAnimation();
        animatorSet = new AnimatorSet();

        ValueAnimator slidingAnimator = ValueAnimator.ofInt(0, parentLeft);
        slidingAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                targetLeft = (int) animator.getAnimatedValue();
                invalidate();
            }
        });
        slidingAnimator.setInterpolator(new LinearInterpolator());
        slidingAnimator.setDuration(ratioAnimationTotalDuration);


        AnimatorSet bounceSet = new AnimatorSet();
        List<Animator> bounceAnimatorList = new ArrayList<>();

        ValueAnimator initialDownAnimator = ValueAnimator.ofFloat(0F, 1F);
        initialDownAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                bounceFraction = (float) animator.getAnimatedValue();
            }
        });
        bounceAnimatorList.add(initialDownAnimator);

        for (int i = 1; i <= indexToParent; i++) {

            ValueAnimator upAnimator = ValueAnimator.ofFloat(1F, 0F);
            upAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    bounceFraction = (float) animator.getAnimatedValue();
                }
            });
            upAnimator.setInterpolator(new DecelerateInterpolator());

            bounceAnimatorList.add(upAnimator);
            ValueAnimator downAnimator = ValueAnimator.ofFloat(0F, 1F);
            downAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    bounceFraction = (float) animator.getAnimatedValue();
                }
            });
            downAnimator.setInterpolator(new AccelerateInterpolator());

            bounceAnimatorList.add(downAnimator);
        }

        int size = bounceAnimatorList.size();
        for (Animator animator : bounceAnimatorList) {
            animator.setDuration(ratioAnimationTotalDuration / size);
        }
        bounceSet.playSequentially(bounceAnimatorList);
        animatorSet.playTogether(slidingAnimator, bounceSet);
        animatorSet.start();
    }

    @Override
    public void stopDotAnimation() {
        if (animatorSet != null) {
            animatorSet.isStarted();
            animatorSet.cancel();
        }
    }

    @Override
    public boolean isAnimating() {
        if (animatorSet != null && animatorSet.isStarted()) {
            return true;
        }

        return false;
    }

    @Override
    protected void setMaxCompressRatio(@FloatRange(from = 0.0, to = 1.0) float compressRatio) {
        // not needed
    }

    private int findIndexToParent(@NonNull ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            if (parent.getChildAt(i) == this) {
                return i;
            }
        }
        return -1;
    }
}
