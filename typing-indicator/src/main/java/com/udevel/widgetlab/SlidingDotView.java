package com.udevel.widgetlab;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.FloatRange;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public class SlidingDotView extends DotView {
    private final Paint paint = new Paint();
    private final Rect clipRect = new Rect();

    private int centerX;
    private int centerY;
    private float radius;
    private boolean isAnimatingDisappear = true;
    private int parentLeft;
    private int parentRight;
    private int targetLeft;
    private int parentWidth;

    private AnimatorSet animatorDisappearSet;
    private AnimatorSet animatorAppearSet;

    public SlidingDotView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(dotColor);
        canvas.getClipBounds(clipRect);
        clipRect.inset(-parentLeft + targetLeft, 0);
        canvas.save();
        canvas.translate(-parentLeft + targetLeft, 0);
        canvas.drawCircle(centerX, centerY, radius, paint);
        canvas.restore();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        parentLeft = left + getWidth();
        parentWidth = ((View) getParent()).getWidth();
        parentRight = parentWidth - left;
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        radius = Math.min(centerX, centerY);
    }

    @Override
    protected void init() {
        // not needed
    }

    @Override
    public void startDotAnimation() {
        int width = getWidth();
        float ratioDxLeft = (parentLeft - width / 2F) / (parentWidth / 2F);
        float ratioDxRight = (parentRight - width / 2F) / (parentWidth / 2F);

        stopDotAnimation();
        if (isAnimatingDisappear) {
            isAnimatingDisappear = false;
            if (animatorAppearSet == null) {
                animatorAppearSet = new AnimatorSet();

                ValueAnimator appearAnimator = ValueAnimator.ofInt(0, parentLeft);
                appearAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        targetLeft = (int) animator.getAnimatedValue();
                        invalidate();
                    }
                });
                appearAnimator.setDuration((long) (animationTotalDuration * Math.sqrt(ratioDxLeft)));
                appearAnimator.setInterpolator(new DecelerateInterpolator());

                ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), dotSecondColor, dotFirstColor);
                colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        dotColor = (int) animator.getAnimatedValue();
                    }
                });
                colorAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dotColor = dotFirstColor;
                    }
                });
                colorAnimator.setDuration(animationTotalDuration);
                colorAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                animatorAppearSet.playTogether(appearAnimator, colorAnimator);
            }
            animatorAppearSet.start();
        } else {
            isAnimatingDisappear = true;
            if (animatorDisappearSet == null) {
                animatorDisappearSet = new AnimatorSet();
                ValueAnimator disappearAnimator = ValueAnimator.ofInt(parentLeft, parentLeft + parentRight + width);
                disappearAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        targetLeft = (int) animator.getAnimatedValue();
                        invalidate();
                    }
                });
                disappearAnimator.setDuration((long) (animationTotalDuration * Math.sqrt(ratioDxRight)));
                disappearAnimator.setInterpolator(new AccelerateInterpolator());

                animatorDisappearSet.play(disappearAnimator);
            }
            animatorDisappearSet.start();
        }
    }

    @Override
    public void stopDotAnimation() {
        if (animatorDisappearSet != null && animatorDisappearSet.isStarted()) {
            animatorDisappearSet.cancel();
        }

        if (animatorAppearSet != null && animatorAppearSet.isStarted()) {
            animatorAppearSet.cancel();
        }
    }

    @Override
    public boolean isAnimating() {
        return (animatorDisappearSet != null && animatorDisappearSet.isStarted())
                || (animatorAppearSet != null && animatorAppearSet.isStarted());
    }

    @Override
    protected void setMaxCompressRatio(@FloatRange(from = 0.0, to = 1.0) float compressRatio) {
        // not needed
    }
}
