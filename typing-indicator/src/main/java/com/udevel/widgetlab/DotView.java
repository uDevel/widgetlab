package com.udevel.widgetlab;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.view.View;

/**
 * Created by benny_zw on 5/21/2017.
 */

abstract class DotView extends View {
    @ColorInt
    protected int dotFirstColor = Color.RED;
    protected int dotSecondColor = Color.BLUE;
    protected int dotColor = Color.TRANSPARENT;
    protected long animationTotalDuration = 600L;

    public DotView(Context context) {
        super(context);
        init();
    }

    public void setColor(@ColorInt int color) {
        dotFirstColor = color;
        dotColor = dotFirstColor;
    }

    public void setSecondColor(@ColorInt int color) {
        dotSecondColor = color;
    }

    public void setAnimationDuration(long duration) {
        animationTotalDuration = duration;
    }

    protected abstract void init();

    protected abstract void startDotAnimation();

    protected abstract void stopDotAnimation();

    protected abstract boolean isAnimating();

    protected abstract void setMaxCompressRatio(@FloatRange(from = 0.0, to = 1.0) float compressRatio);
}
