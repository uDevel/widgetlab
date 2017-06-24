package com.udevel.widgetlab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.udevel.widgetlab.definitions.AnimationType;
import com.udevel.widgetlab.definitions.BackgroundType;
import com.udevel.widgetlab.definitions.Order;
import com.udevel.widgetlab.sequences.CircularSequence;
import com.udevel.widgetlab.sequences.InSequence;
import com.udevel.widgetlab.sequences.RandomNoRepetitionSequence;
import com.udevel.widgetlab.sequences.RandomSequence;
import com.udevel.widgetlab.sequences.ReverseSequence;
import com.udevel.widgetlab.sequences.SequenceGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TypingIndicatorView extends LinearLayout {
    private static final String TAG = TypingIndicatorView.class.getSimpleName();

    private static final int BACKGROUND_TYPE_DEF_VALUE = BackgroundType.SQUARE;
    private static final int BACKGROUND_COLOR_DEF_VALUE = Color.LTGRAY;
    private static final int DOT_ANIMATION_TYPE_DEF_VALUE = AnimationType.GROW;
    private static final int DOT_COUNT_DEF_VALUE = 3;
    private static final int DOT_SIZE_DEF_VALUE = 24;
    private static final int DOT_COLOR_DEF_VALUE = Color.LTGRAY;
    private static final float DOT_MAX_COMPRESS_RATIO_DEF_VALUE = 0.5F;
    private static final int DOT_ANIMATION_DURATION_DEF_VALUE = 600;
    private static final int DOT_HORIZONTAL_SPACING_DEF_VALUE = 20;
    private static final int ANIMATE_ORDER_DEF_VALUE = Order.RANDOM;
    private static final int ANIMATE_FREQUENCY_DEF_VALUE = 1000;

    private final Handler handler = new Handler();
    private final Random random = new Random();
    private final List<DotView> dotViewList = new ArrayList<>();

    @IntRange(from = 1)
    private int numOfDots;

    private boolean isAnimationStarted;

    @IntRange(from = 0)
    private int dotHorizontalSpacing;

    @IntRange(from = 1)
    private int dotSize;

    @FloatRange(from = 0F, to = 1F)
    private float dotMaxCompressRatio;

    private boolean isShowBackground;

    @BackgroundType
    private int backgroundType;

    @ColorInt
    private int backgroundColor;

    @AnimationType
    private int dotAnimationType;

    @ColorInt
    private int dotColor;

    @ColorInt
    private int dotSecondColor;

    @IntRange(from = 1)
    private int dotAnimationDuration;

    @IntRange(from = 1)
    private int animateFrequency;

    private Paint backgroundPaint;
    private SequenceGenerator sequenceGenerator;
    private final Runnable dotAnimationRunnable = new Runnable() {
        @Override
        public void run() {
            int nextAnimateDotIndex = sequenceGenerator.nextIndex(numOfDots);
            dotViewList.get(nextAnimateDotIndex).startDotAnimation();
            long delayMillis = (animateFrequency < 0L) ? (long) (500L + (2000L * random.nextFloat())) : animateFrequency;
            if (isAnimationStarted) {
                handler.postDelayed(dotAnimationRunnable, delayMillis);
            }
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
            if (getVisibility() == VISIBLE) {
                startDotAnimation();
            }
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
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getVisibility() == VISIBLE) {
            startDotAnimation();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopDotAnimation();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int radius = Math.min(getWidth(), getHeight()) / 2;
        switch (backgroundType) {
            case BackgroundType.ROUNDED:
                canvas.drawCircle(radius, radius, radius, backgroundPaint);
                canvas.drawCircle(getWidth() - radius, radius, radius, backgroundPaint);
                canvas.drawRect(radius, 0, getWidth() - radius, getHeight(), backgroundPaint);
                break;
            case BackgroundType.SQUARE:
            default:
                canvas.drawRect(0, 0, getWidth(), getHeight(), backgroundPaint);
                break;
        }
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
        int animationOrder;

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

        if (dotAnimationType == AnimationType.DISAPPEAR) {
            animationOrder = Order.SEQUENCE;
        }

        setAnimationOrder(animationOrder);
    }

    private void init() {
        setClipToPadding(false);
        setClipChildren(false);
        if (isShowBackground) {
            setWillNotDraw(false);
            backgroundPaint = new Paint();
            backgroundPaint.setColor(backgroundColor);
        }

        int halfHorizontalSpacing = dotHorizontalSpacing / 2;
        for (int i = 0; i < numOfDots; i++) {
            DotView dotView = createDotView(dotAnimationType);
            LinearLayout.LayoutParams layoutParams = new LayoutParams(dotSize, dotSize);
            layoutParams.setMargins(halfHorizontalSpacing, 0, halfHorizontalSpacing, 0);
            addView(dotView, layoutParams);
            dotViewList.add(dotView);
        }
    }

    private DotView createDotView(@AnimationType int dotAnimationType) {
        Context context = getContext();
        DotView dotView;
        switch (dotAnimationType) {
            case AnimationType.BOUNCING_SLIDING:
                dotView = new BouncingSlidingDotView(context);
                break;
            case AnimationType.SLIDING:
                dotView = new SlidingDotView(context);
                break;
            case AnimationType.WINK:
                dotView = new WinkDotView(context);
                break;
            case AnimationType.DISAPPEAR:
                dotView = new DisappearDotView(context);
                break;
            case AnimationType.GROW:
            default:
                dotView = new GrowDotView(context);
                break;
        }

        dotView.setAnimationDuration(dotAnimationDuration);
        dotView.setMaxCompressRatio(dotMaxCompressRatio);
        dotView.setColor(dotColor);
        dotView.setSecondColor(dotSecondColor);
        return dotView;
    }

    public void setAnimationOrder(@Order int animationOrder) {
        switch (animationOrder) {
            case Order.CIRCULAR:
                sequenceGenerator = new CircularSequence();
            case Order.LAST_FIRST:
                sequenceGenerator = new ReverseSequence();
            case Order.RANDOM_NO_REPETITION:
                sequenceGenerator = new RandomNoRepetitionSequence();
            case Order.SEQUENCE:
                sequenceGenerator = new InSequence();
            case Order.RANDOM:
                sequenceGenerator = new RandomSequence();
        }
    }
}
