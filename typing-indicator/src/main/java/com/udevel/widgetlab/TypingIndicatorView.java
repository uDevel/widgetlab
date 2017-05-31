package com.udevel.widgetlab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
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
    public static final int ANIMATE_ORDER_CIRCULAR = 2;
    public static final int ANIMATE_ORDER_LAST_FIRST = 3;
    public static final int BACKGROUND_TYPE_ROUNDED = 1;
    public static final int BACKGROUND_TYPE_SQUARE = 0;
    public static final int DOT_ANIMATION_GROW = 0;
    public static final int DOT_ANIMATION_WINK = 1;
    public static final int DOT_ANIMATION_DISAPPEAR = 2;
    public static final int DOT_ANIMATION_SLIDING = 3;
    public static final int DOT_ANIMATION_BOUNCING_SLIDING = 4;
    private static final String TAG = TypingIndicatorView.class.getSimpleName();
    private static final int BACKGROUND_TYPE_DEF_VALUE = BACKGROUND_TYPE_SQUARE;
    private static final int DOT_COUNT_DEF_VALUE = 3;
    private static final int DOT_SIZE_DEF_VALUE = 24;
    private static final int DOT_COLOR_DEF_VALUE = Color.LTGRAY;
    private static final float DOT_MAX_COMPRESS_RATIO_DEF_VALUE = 0.5F;
    private static final int DOT_ANIMATION_DURATION_DEF_VALUE = 600;
    private static final int DOT_ANIMATION_TYPE_DEF_VALUE = DOT_ANIMATION_GROW;
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
    private int nextAnimateDotIndex = 0;
    private boolean animateDotIndexDirectionPositive = true;

    private Runnable dotAnimationRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "run() called");
            int dotsCount = dotViewList.size();

            int animateDotIndex = 0;
            switch (animationOrder) {
                case ANIMATE_ORDER_RANDOM:
                    animateDotIndex = random.nextInt(dotsCount);
                    break;
                case ANIMATE_ORDER_SEQUENCE:
                    animateDotIndex = nextAnimateDotIndex;
                    nextAnimateDotIndex++;
                    if (nextAnimateDotIndex >= dotsCount) {
                        nextAnimateDotIndex = 0;
                    }
                    break;
                case ANIMATE_ORDER_LAST_FIRST:
                    animateDotIndex = dotsCount - 1 - nextAnimateDotIndex;
                    nextAnimateDotIndex++;
                    if (nextAnimateDotIndex >= dotsCount) {
                        nextAnimateDotIndex = 0;
                    }
                    break;
                case ANIMATE_ORDER_CIRCULAR:
                    animateDotIndex = nextAnimateDotIndex;

                    if (animateDotIndexDirectionPositive) {
                        nextAnimateDotIndex++;
                        if (nextAnimateDotIndex >= dotsCount - 1) {
                            animateDotIndexDirectionPositive = false;
                        }
                    } else {
                        nextAnimateDotIndex--;
                        if (nextAnimateDotIndex <= 0) {
                            animateDotIndexDirectionPositive = true;
                        }
                    }
                    break;

            }

            dotViewList.get(animateDotIndex).startDotAnimation();
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
        super(context, attrs, defStyleAttr);
        parseAttributes(context, attrs);
        init();
    }

    public static Path composeRoundedRectPath(float left, float top, float right, float bottom, float radius) {
        Log.d(TAG, "composeRoundedRectPath() called with: left = [" + left + "], top = [" + top + "], right = [" + right + "], bottom = [" + bottom + "], radius = [" + radius + "]");
        Path path = new Path();

        path.moveTo(left + radius, top);
        path.lineTo(right - radius, top);
        path.quadTo(right - radius / 2, top - radius / 2, right, top + radius);
        path.lineTo(right, bottom - radius);
        path.quadTo(right, bottom, right - radius, bottom);
        path.lineTo(left + radius, bottom);
        path.quadTo(left, bottom, left, bottom - radius);
        path.lineTo(left, top + radius);
        path.quadTo(left, top, left + radius, top);
        path.close();

        return path;
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
        int radius = Math.min(getWidth(), getHeight()) / 2;
        switch (backgroundType) {
            case BACKGROUND_TYPE_ROUNDED:
                canvas.drawCircle(radius, radius, radius, backgroundPaint);
                canvas.drawCircle(getWidth() - radius, radius, radius, backgroundPaint);
                canvas.drawRect(radius, 0, getWidth() - radius, getHeight(), backgroundPaint);
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
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TypingIndicatorView, 0, 0);

        try {
            dotSize = a.getDimensionPixelOffset(R.styleable.TypingIndicatorView_dotSize, DOT_SIZE_DEF_VALUE);
            numOfDots = a.getInteger(R.styleable.TypingIndicatorView_dotCount, DOT_COUNT_DEF_VALUE);
            dotHorizontalSpacing = a.getDimensionPixelOffset(R.styleable.TypingIndicatorView_dotHorizontalSpacing, DOT_HORIZONTAL_SPACING_DEF_VALUE);
            dotColor = a.getColor(R.styleable.TypingIndicatorView_dotColor, DOT_COLOR_DEF_VALUE);
            dotSecondColor = a.getColor(R.styleable.TypingIndicatorView_dotSecondColor, dotColor);
            dotMaxCompressRatio = a.getFraction(R.styleable.TypingIndicatorView_dotMaxCompressRatio, 1, 1, DOT_MAX_COMPRESS_RATIO_DEF_VALUE);
            dotAnimationDuration = a.getInteger(R.styleable.TypingIndicatorView_dotAnimationDuration, DOT_ANIMATION_DURATION_DEF_VALUE);
            dotAnimationType = a.getInteger(R.styleable.TypingIndicatorView_dotAnimationType, DOT_ANIMATION_TYPE_DEF_VALUE);
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

        if (dotAnimationType == DOT_ANIMATION_DISAPPEAR && animationOrder != ANIMATE_ORDER_SEQUENCE) {
            animationOrder = ANIMATE_ORDER_SEQUENCE;
        }
    }

    private void init() {
        setClipToPadding(false);
        setClipChildren(false);
        if (isShowBackground) {
            setWillNotDraw(false);
            backgroundPaint = new Paint();
            backgroundPaint.setColor(backgroundColor);
        }

        for (int i = 0; i < numOfDots; i++) {
            DotView dotView;
            switch (dotAnimationType) {
                case DOT_ANIMATION_BOUNCING_SLIDING:
                    dotView = new BouncingSlidingDotView(getContext());
                    break;
                case DOT_ANIMATION_SLIDING:
                    dotView = new SlidingDotView(getContext());
                    break;
                case DOT_ANIMATION_WINK:
                    dotView = new WinkDotView(getContext());
                    break;
                case DOT_ANIMATION_DISAPPEAR:
                    dotView = new DisappearDotView(getContext());
                    break;
                case DOT_ANIMATION_GROW:
                default:
                    dotView = new GrowDotView(getContext());
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
