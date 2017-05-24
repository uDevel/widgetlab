package com.udevel.widgetlab;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.FloatRange;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public class BouncingSlidingDotView extends DotView {
    private static final String TAG = BouncingSlidingDotView.class.getSimpleName();
    private Paint paint = new Paint();
    private int centerX;
    private int centerY;
    private float radius;
    private boolean isAnimatingDisappear = true;
    private AnimatorSet animatorDisappearSet;
    private AnimatorSet animatorAppearSet;
    private int parentLeft;
    private int parentRight;
    private int targetLeft;
    private Rect clipRect = new Rect();
    private int parentWidth;

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
        canvas.drawCircle(centerX, centerY, radius, paint);
        canvas.restore();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        parentLeft = left + getWidth();
        parentWidth = ((View) getParent()).getWidth();
        parentRight = parentWidth - getWidth() - left;
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        radius = (Math.min(getWidth(), getHeight()) / 2) / 1.2F;    // Save space for overshoot interpolator.
    }

    @Override
    protected void init() {
        paint = new Paint();
    }

    @Override
    public void startDotAnimation() {
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
                appearAnimator.setDuration(animationTotalDuration);
                appearAnimator.setInterpolator(new DecelerateInterpolator());
                animatorAppearSet.playTogether(appearAnimator);
            }
            animatorAppearSet.start();
        } else {
            isAnimatingDisappear = true;
            if (animatorDisappearSet == null) {
                animatorDisappearSet = new AnimatorSet();
                ValueAnimator disappearAnimator = ValueAnimator.ofInt(parentLeft, parentLeft + parentRight + getWidth());
                disappearAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        targetLeft = (int) animator.getAnimatedValue();
                        invalidate();
                    }
                });
                disappearAnimator.setDuration(animationTotalDuration);
                disappearAnimator.setInterpolator(new AccelerateInterpolator());

                animatorDisappearSet.play(disappearAnimator);
            }
            animatorDisappearSet.start();
        }
    }

    @Override
    public void stopDotAnimation() {
        if (animatorDisappearSet != null) {
            animatorDisappearSet.isStarted();
            animatorDisappearSet.cancel();
        }
        if (animatorAppearSet != null) {
            animatorAppearSet.isStarted();
            animatorAppearSet.cancel();
        }
    }

    @Override
    public boolean isAnimating() {
        if (animatorDisappearSet != null && animatorDisappearSet.isStarted()) {
            return true;
        }

        if (animatorAppearSet != null && animatorAppearSet.isStarted()) {
            return true;
        }

        return false;
    }

    @Override
    protected void setMaxCompressRatio(@FloatRange(from = 0.0, to = 1.0) float compressRatio) {
        // not needed
    }
}
