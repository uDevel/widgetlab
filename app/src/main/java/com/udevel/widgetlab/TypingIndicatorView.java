package com.udevel.widgetlab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class TypingIndicatorView extends LinearLayout {
    public static final int ANIMATE_ORDER_RANDOM = 0;
    public static final int ANIMATE_ORDER_SEQUENCE = 1;
    public static final int ANIMATE_ORDER_SEQUENCE_REVERSAL = 2;
    public static final int BACKGROUND_TYPE_ROUNDED = 1;
    public static final int BACKGROUND_TYPE_SQUARE = 0;
    public static final int DOT_ANIMATION_WINK = 0;
    public static final int DOT_ANIMATION_GROW = 1;
    private static final String TAG = TypingIndicatorView.class.getSimpleName();
    private static final int BACKGROUND_TYPE_DEF_VALUE = BACKGROUND_TYPE_SQUARE;
    private static final int DOT_COUNT_DEF_VALUE = 3;
    private static final int DOT_SIZE_DEF_VALUE = 50;
    private static final int DOT_COLOR_DEF_VALUE = Color.RED;
    private static final float DOT_MAX_COMPRESS_RATIO_DEF_VALUE = 0.5F;
    private static final int DOT_ANIMATION_DURATION_DEF_VALUE = 600;
    private static final int DOT_ANIMATION_TYPE_DEF_VALUE = DOT_ANIMATION_WINK;
    private static final int DOT_HORIZONTAL_SPACING_DEF_VALUE = 20;
    private static final int BACKGROUND_COLOR_DEF_VALUE = Color.LTGRAY;
    private static final int ANIMATE_ORDER_DEF_VALUE = ANIMATE_ORDER_RANDOM;
    private static final int ANIMATE_FREQUENCY_DEF_VALUE = 1000;
    private Handler handler = new Handler();
    private List<DotView> dotViewList = new ArrayList<>();
    private int numOfDots;
    private Random random = new Random();
    private boolean isAnimationStarted;
    private int dotHorizontalSpacing;
    private int dotSize;
    private float dotMaxCompressRatio;
    private Paint backgroundPaint;
    private boolean isShowBackground;
    private int backgroundType;
    private int backgroundColor;
    private int dotColor;
    private int dotSecondColor;
    private int dotAnimationDuration;
    private int dotAnimationType;
    private int animateFrequency;
    private int animationOrder;
    private int nextAnimateDotIndex;
    private boolean animateDotIndexDirectionPositive = true;

    private Runnable dotAnimationRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "run() called");
            int dotsCount = dotViewList.size();


            switch (animationOrder) {
                case ANIMATE_ORDER_RANDOM:
                    nextAnimateDotIndex = random.nextInt(dotsCount);
                    break;
                case ANIMATE_ORDER_SEQUENCE:
                    nextAnimateDotIndex++;
                    if (nextAnimateDotIndex == dotsCount) {
                        nextAnimateDotIndex = 0;
                    }
                    break;
                case ANIMATE_ORDER_SEQUENCE_REVERSAL:
                    if (animateDotIndexDirectionPositive) {
                        nextAnimateDotIndex++;
                        if (nextAnimateDotIndex == dotsCount - 1) {
                            animateDotIndexDirectionPositive = false;
                        }
                    } else {
                        nextAnimateDotIndex--;
                        if (nextAnimateDotIndex == 0) {
                            animateDotIndexDirectionPositive = true;
                        }
                    }
                    break;
            }

            dotViewList.get(nextAnimateDotIndex).startDotAnimation();
            long delayMillis;
            if (animateFrequency < 0L) {
                delayMillis = (long) (500L + (2000L * random.nextFloat()));
            } else {
                delayMillis = animateFrequency;
            }

            handler.postDelayed(dotAnimationRunnable, delayMillis);
        }
    };


    public TypingIndicatorView(Context context) {
        this(context, null);
    }

    public TypingIndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TypingIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TypingIndicatorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        parseAttributes(context, attrs);
        init();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            startDotAnimation();
        } else {
            stopDotAnimation();
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            startDotAnimation();
        } else {
            stopDotAnimation();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int radius = Math.min(getWidth(), getHeight());
        switch (backgroundType) {
            case BACKGROUND_TYPE_ROUNDED:
                canvas.drawRoundRect(0, 0, getWidth(), getHeight(), radius, radius, backgroundPaint);
                break;
            case BACKGROUND_TYPE_SQUARE:
                canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundPaint);
                break;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopDotAnimation();
    }

    @UiThread
    public void stopDotAnimation() {
        isAnimationStarted = false;
        handler.removeCallbacks(dotAnimationRunnable);
    }

    @UiThread
    public void startDotAnimation() {
        if (isAnimationStarted) {
            return;
        }

        isAnimationStarted = true;
        handler.post(dotAnimationRunnable);
    }

    public boolean isAnimationStarted() {
        return isAnimationStarted;
    }

    private void parseAttributes(@NonNull Context context, @Nullable AttributeSet attrs) {
      /*      <attr name="dotSize" format="integer"/>
        <attr name="dotColor" format="color"/>
        <attr name="dotHorizontalSpacing" format="dimension"/>
        <attr name="showBackground" format="boolean"/>
        <attr name="backgroundColor" format="color"/>
        <attr name="backgroundType" format="enum">
            <enum name="square" value="0"/>
            <enum name="rounded" value="1"/>
        </attr>*/

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TypingIndicatorView, 0, 0);

        try {
            dotSize = a.getDimensionPixelOffset(R.styleable.TypingIndicatorView_dotSize, DOT_SIZE_DEF_VALUE);
            numOfDots = a.getInteger(R.styleable.TypingIndicatorView_dotCount, DOT_COUNT_DEF_VALUE);
            dotHorizontalSpacing = a.getDimensionPixelOffset(R.styleable.TypingIndicatorView_dotHorizontalSpacing, DOT_HORIZONTAL_SPACING_DEF_VALUE);
            dotColor = a.getColor(R.styleable.TypingIndicatorView_dotColor, DOT_COLOR_DEF_VALUE);
            dotSecondColor = a.getColor(R.styleable.TypingIndicatorView_dotSecondColor, dotColor);
            dotMaxCompressRatio = a.getFraction(R.styleable.TypingIndicatorView_dotMaxCompressRatio, 1, 1, DOT_MAX_COMPRESS_RATIO_DEF_VALUE);
            dotAnimationDuration = a.getInteger(R.styleable.TypingIndicatorView_dotAnimationDuration, DOT_ANIMATION_DURATION_DEF_VALUE);
            dotAnimationType = a.getInteger(R.styleable.TypingIndicatorView_dotAnimationType, DOT_ANIMATION_WINK);
            isShowBackground = a.getBoolean(R.styleable.TypingIndicatorView_showBackground, false);
            backgroundType = a.getInteger(R.styleable.TypingIndicatorView_backgroundType, BACKGROUND_TYPE_DEF_VALUE);
            backgroundColor = a.getColor(R.styleable.TypingIndicatorView_backgroundColor, BACKGROUND_COLOR_DEF_VALUE);
            animationOrder = a.getInteger(R.styleable.TypingIndicatorView_animationOrder, ANIMATE_ORDER_DEF_VALUE);
            animateFrequency = a.getInteger(R.styleable.TypingIndicatorView_animateFrequency, Math.max(dotAnimationDuration, ANIMATE_FREQUENCY_DEF_VALUE));
        } finally {
            a.recycle();
        }

        if (dotMaxCompressRatio > 1F || dotMaxCompressRatio < 0F) {
            throw new IllegalArgumentException("dotMaxCompressRatio must be between 0% and 100%");
        }
    }

    private void init() {
        if (isShowBackground) {
            setWillNotDraw(false);
            backgroundPaint = new Paint();
            backgroundPaint.setColor(backgroundColor);
        }

       /* for (int i = 0; i < numOfDots; i++) {
            WinkDotView winkDotView = new WinkDotView(getContext());
            winkDotView.setMaxCompressRatio(dotMaxCompressRatio);
            winkDotView.setColor(dotColor);
            winkDotView.setSecondColor(dotSecondColor);
            LinearLayout.LayoutParams layoutParams = new LayoutParams(dotSize, dotSize);
            int halfHorizontalSpacing = dotHorizontalSpacing / 2;
            layoutParams.setMargins(halfHorizontalSpacing, 0, halfHorizontalSpacing, 0);

            addView(winkDotView, layoutParams);
            dotViewList.add(winkDotView);
        }*/

        for (int i = 0; i < numOfDots; i++) {
            DotView dotView;
            switch (dotAnimationType) {
                case DOT_ANIMATION_WINK:
                    dotView = new GrowDotView(getContext());
                    break;
                case DOT_ANIMATION_GROW:
                default:
                    dotView = new WinkDotView(getContext());
                    break;
            }

            dotView.setAnimationDuration(dotAnimationDuration);
            dotView.setMaxCompressRatio(dotMaxCompressRatio);
            dotView.setColor(dotColor);
            dotView.setSecondColor(dotSecondColor);
            LinearLayout.LayoutParams layoutParams = new LayoutParams(dotSize, dotSize);
            int halfHorizontalSpacing = dotHorizontalSpacing / 2;
            layoutParams.setMargins(halfHorizontalSpacing, 0, halfHorizontalSpacing, 0);

            addView(dotView, layoutParams);
            dotViewList.add(dotView);
        }
    }
}
