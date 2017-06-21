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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;

import java.util.ArrayList;
import java.util.List;

public class WinkDotView extends DotView {
    @FloatRange(from = 0, to = 1F)
    private static final float RATIO_OF_HORIZONTAL_EXPANSION = 0.33F;

    private int centerX;
    private int centerY;

    private float radius;
    private float scaleY = 1F;
    private float scaleX = 1F;
    private float minScaleY = 0.6F;

    private Paint paint;
    private RectF ovalRectF;

    @Nullable
    private AnimatorSet animatorSet;

    public WinkDotView(@NonNull Context context) {
        super(context);
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
        radius = Math.min(centerX, centerY) / (1 + ((1 - minScaleY) / 2F));
        updateOvalRecF();
    }

    @Override
    protected void init() {
        paint = new Paint();
        ovalRectF = new RectF();
    }

    @Override
    public void startDotAnimation() {
        stopDotAnimation();

        // TODO: Fix bug for when the color of the dots change, we need to reset the AnimatorSet
        if (animatorSet == null) {
            animatorSet = new AnimatorSet();
            List<Animator> animations = new ArrayList<>(2);
            ValueAnimator winkAnimator = ValueAnimator.ofFloat(1F, minScaleY);
            winkAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scaleY = (float) animation.getAnimatedValue();
                    scaleX = 1 + ((1 - scaleY) * RATIO_OF_HORIZONTAL_EXPANSION);
                    updateOvalRecF();
                    invalidate();
                }
            });

            winkAnimator.setDuration(animationTotalDuration / 2);
            winkAnimator.setInterpolator(new FastOutSlowInInterpolator());
            winkAnimator.setRepeatCount(1);
            winkAnimator.setRepeatMode(ValueAnimator.REVERSE);
            animations.add(winkAnimator);

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
                    scaleY = 1F;
                    scaleX = 1F;
                    updateOvalRecF();
                    invalidate();
                }
            });
        }
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
        return animatorSet != null && animatorSet.isStarted();
    }

    public void setMaxCompressRatio(@FloatRange(from = 0.0, to = 1.0) float compressRatio) {
        minScaleY = compressRatio;
    }

    private void updateOvalRecF() {
        float modifiedRadiusX = (radius * scaleX);
        ovalRectF.left = centerX - modifiedRadiusX;
        ovalRectF.right = centerX + modifiedRadiusX;

        ovalRectF.top = centerY - ((2 * radius * scaleY) - radius);
        ovalRectF.bottom = centerY + radius;
    }
}
