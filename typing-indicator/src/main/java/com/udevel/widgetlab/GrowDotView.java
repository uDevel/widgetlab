package com.udevel.widgetlab;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.FloatRange;
import android.support.v4.view.animation.FastOutSlowInInterpolator;

import java.util.ArrayList;
import java.util.List;

public class GrowDotView extends DotView {
    private final Paint paint = new Paint();
    private final RectF ovalRectF = new RectF();

    private int centerX;
    private int centerY;
    private float radius;
    private float targetScale = 0.7F;
    private float scale = targetScale;

    private AnimatorSet animatorSet;

    public GrowDotView(Context context) {
        super(context);
    }

    @Override
    protected void init() {
        // nothing to init.
    }

    @Override
    public void startDotAnimation() {
        stopDotAnimation();

        // TODO: Fix bug for when the color of the dots change, we need to reset the AnimatorSet
        if (animatorSet == null) {
            animatorSet = new AnimatorSet();
            List<Animator> animations = new ArrayList<>(2);

            ValueAnimator growAnimator = ValueAnimator.ofFloat(targetScale, 1.0F);
            growAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scale = (float) animation.getAnimatedValue();
                    updateOvalRecF();
                    invalidate();
                }
            });

            growAnimator.setDuration(animationTotalDuration / 2);
            growAnimator.setInterpolator(new FastOutSlowInInterpolator());
            growAnimator.setRepeatCount(1);
            growAnimator.setRepeatMode(ValueAnimator.REVERSE);
            animations.add(growAnimator);

            if (dotFirstColor != dotSecondColor) {
                ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), dotFirstColor, dotSecondColor);
                colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        dotColor = (int) animator.getAnimatedValue();
                    }

                });
                colorAnimator.setDuration(animationTotalDuration / 2);
                colorAnimator.setInterpolator(new FastOutSlowInInterpolator());
                colorAnimator.setRepeatCount(1);
                colorAnimator.setRepeatMode(ValueAnimator.REVERSE);
                animations.add(colorAnimator);
            }

            animatorSet.playTogether(animations);
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    dotColor = dotFirstColor;
                    scale = targetScale;
                    updateOvalRecF();
                    invalidate();
                }
            });
        }
        animatorSet.start();
    }

    @Override
    public void stopDotAnimation() {
        if (isAnimating()) {
            animatorSet.cancel();
        }
    }

    @Override
    public boolean isAnimating() {
        return animatorSet != null && animatorSet.isStarted();
    }

    @Override
    public void setMaxCompressRatio(@FloatRange(from = 0.0, to = 1.0) float compressRatio) {
        targetScale = compressRatio;
        scale = targetScale;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(dotColor);
        canvas.drawOval(ovalRectF, paint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        radius = (Math.min(getWidth(), getHeight()) / 2);
        updateOvalRecF();
    }

    private void updateOvalRecF() {
        ovalRectF.left = centerX - (radius * scale);
        ovalRectF.top = centerY - (radius * scale);
        ovalRectF.right = centerX + (radius * scale);
        ovalRectF.bottom = centerY + (radius * scale);
    }
}
