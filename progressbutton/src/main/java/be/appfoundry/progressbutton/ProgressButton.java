/*
 * Copyright 2016 AppFoundry
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package be.appfoundry.progressbutton;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.Locale;

import be.appfoundry.progressbutton.util.CircularOutline;

/**
 * A circular progress button with customizable color and animation.
 */
public class ProgressButton extends View {

    private static final float DEFAULT_MAX_PROGRESS = 100;
    private static final float DEFAULT_ANIMATION_STEP = 1.0f;
    private static final int DEFAULT_START_DEGREES = 270;
    private static final int DEFAULT_ANIMATION_DELAY = 0;
    private static final int MAX_DEGREES = 360;
    private static final int MIN_SIZE = 48;

    /** The background color of the button. */
    private int color;
    /** The stroke color of the button. */
    private int strokeColor;
    /** The color of the progress indicator. */
    private int progressColor;
    private float strokeWidth;
    /** The maximum progress. Defaults to 100. */
    private float maxProgress = DEFAULT_MAX_PROGRESS;
    /** The value for each animation step. Defaults to 1 */
    private float animationStep = DEFAULT_ANIMATION_STEP;
    /** Sets the button indeterminate or determinate. Defaults to true. */
    private boolean indeterminate;
    /** Sets the direction of the progress indicator. */
    private boolean reverse = false;
    /** The icon on the button. */
    private Drawable icon;
    /** The radius of the outer circle. */
    private float radius;
    /** The current progress of the progress indicator. */
    private float progress;
    /** indicates if the indeterminate progress animation is running. Defaults to false. */
    private boolean isAnimating = false;
    /** The starting position for the progress indicator. */
    private float startDegrees = DEFAULT_START_DEGREES;
    /** The current starting point of the progress animation. */
    private float startingPoint = DEFAULT_START_DEGREES;
    /** Delay between animation frames. Defaults to 0. */
    private int animationDelay = DEFAULT_ANIMATION_DELAY;
    /** The Paint for the inner circle. */
    private Paint circlePaint;
    /** The Paint for the progress indicator. */
    private Paint progressPaint;
    /** The Paint for the outer circle. */
    private Paint strokePaint;
    /** The amount of degrees for indicating the progress. */
    private float degrees;
    /** The handler for the indeterminate progress animation */
    private Handler animationHandler = new AnimationHandler(this);
    /** The rectangle for drawing the button. */
    RectF buttonRectF = new RectF();
    /** The rectangle for drawing the icon. */
    Rect iconRect = new Rect();


    public ProgressButton(Context context) {
        this(context, null);
        init(context, null);
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }

    public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * Initialise the {@link ProgressButton}.
     *
     * @param context the application environment
     * @param attrs Attribute Set provided
     */
    private void init(Context context, AttributeSet attrs) {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        TypedArray attr = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.ProgressButton, 0, 0);

        try {
            color = attr.getColor(R.styleable.ProgressButton_fillColor, 0xFFFFFFFF);
            circlePaint.setColor(color);
            strokeColor = attr.getColor(R.styleable.ProgressButton_strokeColor, 0xFFFFFFFF);
            strokePaint.setColor(strokeColor);
            progressColor = attr.getColor(R.styleable.ProgressButton_progressColor, 0xFFFFFFFF);
            progressPaint.setColor(progressColor);
            strokeWidth = attr.getDimension(R.styleable.ProgressButton_strokeWidth, 0f);
            indeterminate = attr.getBoolean(R.styleable.ProgressButton_indeterminate, true);
            icon = attr.getDrawable(R.styleable.ProgressButton_progressIcon);
        } finally {
            attr.recycle();
        }
    }

    /** Returns true if the progress is indeterminate. */
    public boolean isIndeterminate() {
        return indeterminate;
    }

    /** Sets if the progress is indeterminate or determinate. */
    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
        invalidate();
    }

    /** Returns the maximum progress value of the indicator. */
    public float getMaxProgress() {
        return maxProgress;
    }

    /** Sets the maximus progress value of the indicator. */
    public void setMaxProgress(float maxProgress) {
        this.maxProgress = maxProgress;
    }

    /** Returns the current progress. */
    public float getProgress() {
        return progress;
    }

    /** Sets the current progress. (must be between 0 and maxProgress) */
    public void setProgress(float progress) {
        if (progress > maxProgress || progress < 0) {
            throw new IllegalArgumentException(String.format(
                    Locale.getDefault(),
                    "Progress (%d) must be between %d and %d",
                    progress,
                    0,
                    maxProgress
            ));
        }
        this.progress = progress;
        degrees = MAX_DEGREES * progress / maxProgress;
        invalidate();
    }

    /** Returns the starting point of the progress indicator. */
    public float getStartDegrees() {
        return startDegrees;
    }

    /** Sets the starting point of the progress indicator. */
    public void setStartDegrees(float degrees) {
        this.startDegrees = degrees;
        this.startingPoint = degrees;
        invalidate();
    }

    /** returns the button icon. */
    public Drawable getIcon() {
        return icon;
    }

    /** Sets the button icon. */
    public void setIcon(@IdRes Drawable icon) {
        this.icon = icon;
        invalidate();
    }

    /** Returns the background color of the button. */
    public int getColor() {
        return color;
    }

    /** Sets the background color of the button. */
    public void setColor(@ColorInt int color) {
        this.color = color;
        circlePaint.setColor(color);
        invalidate();
    }

    /** Returns the stroke color. */
    public int getStrokeColor() {
        return strokeColor;
    }

    /** Sets the stroke color. */
    public void setStrokeColor(@ColorInt int strokeColor) {
        this.strokeColor = strokeColor;
        strokePaint.setColor(strokeColor);
        invalidate();
    }

    /** Returns the color of the progress indicator. */
    public int getProgressColor() {
        return progressColor;
    }

    /** Sets the color of the progress indicator. */
    public void setProgressColor(@ColorInt int progressColor) {
        this.progressColor = progressColor;
        progressPaint.setColor(progressColor);
        invalidate();
    }

    /** Returns the value for each animation step. */
    public float getAnimationStep() {
        return animationStep;
    }

    /** Sets the value for each animation step. */
    public void setAnimationStep(float animationStep) {
        this.animationStep = animationStep;
    }

    /** Returns the animation delay. */
    public float getAnimationDelay() {
        return animationDelay;
    }

    /** Sets the animation delay. */
    public void setAnimationDelay(int animationDelay) {
        this.animationDelay = animationDelay;
    }

    /** Returns the radius of the button. */
    public float getRadius() {
        return radius;
    }

    /** Sets the radius of the button. */
    public void setRadius(float radius) {
        this.radius = radius;
        invalidate();
    }

    /** Returns the stroke width. */
    public float getStrokeWidth() {
        return strokeWidth;
    }

    /** Sets the stroke width. */
    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);

        int width = getLayoutParams().width;
        int height = getLayoutParams().height;

        int getWidth = getWidth();
        int getHeight = getHeight();

        int resolvedWidth = resolveSize(getWidth, widthMeasureSpec);
        int resolvedHeight = resolveSize(getHeight, heightMeasureSpec);

        int size = calculateSize(resolvedWidth, resolvedHeight, width, height);

        setMeasuredDimension(size, size);
    }

    /** Calculate the size of the progressbutton */
    private int calculateSize(int resolvedWidth, int resolvedHeight, int width, int height) {
        int size;

        if (radius > 0) {
            size = (int) (radius * 2);
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.MATCH_PARENT
                && getLayoutParams().height == ViewGroup.LayoutParams.MATCH_PARENT) {
            size = resolvedWidth;
            if (size > resolvedHeight) {
                size = resolvedHeight;
            }
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.MATCH_PARENT
                && getLayoutParams().height != ViewGroup.LayoutParams.MATCH_PARENT) {
            size = resolvedHeight;
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.MATCH_PARENT
                && getLayoutParams().width != ViewGroup.LayoutParams.MATCH_PARENT) {
            size = resolvedWidth;
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT
                && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            size = (int) convertDpToPixel(this.getContext(), MIN_SIZE);
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT
                && getLayoutParams().height != ViewGroup.LayoutParams.WRAP_CONTENT) {
            size = resolvedHeight;
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT
                && getLayoutParams().width != ViewGroup.LayoutParams.WRAP_CONTENT) {
            size = resolvedWidth;
        } else if (width > height) {
            size = resolvedHeight;
        } else if (width < height) {
            size = resolvedWidth;
        } else {
            size = calculateDefaultSize(resolvedWidth, resolvedHeight, width, height);
        }

        return size;
    }

    /** Calculate the default size of the progressbutton */
    private int calculateDefaultSize(int resolvedWidth, int resolvedHeight, int width, int height) {
        int size;
        if (resolvedHeight == 0) {
            size = width;
            if (size > resolvedWidth) {
                size = resolvedWidth;
            }
        } else if (resolvedWidth == 0) {
            size = height;
            if (size > resolvedHeight) {
                size = resolvedHeight;
            }
        } else {
            size = resolvedWidth;
            if (size > resolvedHeight) {
                size = resolvedHeight;
            }
        }
        return size;
    }

    /** Convert dp value to pixels. */
    private static float convertDpToPixel(Context context, float dp) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setOutlineProvider(new CircularOutline(width, height));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (radius == 0) {
            radius = getWidth() / 2;
        }

        float left = (getWidth() / 2) - radius;
        float right = (getWidth() / 2) + radius;
        float top = (getHeight() / 2) - radius;
        float bottom = (getHeight() / 2) + radius;

        buttonRectF.set(left, top, right, bottom);

        canvas.drawCircle(getWidth() / 2, getWidth() / 2, radius, strokePaint);
        canvas.drawArc(buttonRectF, startingPoint, degrees, true, progressPaint);
        canvas.drawCircle(getWidth() / 2, getWidth() / 2, radius - strokeWidth, circlePaint);

        if (icon != null) {
            iconRect.set(0, 0, (int) radius, (int) radius);
            iconRect.offset((getWidth() - (int) radius) / 2, (getHeight() - (int) radius) / 2);
            icon.setBounds(iconRect);
            icon.draw(canvas);
        }
    }

    /**
     * Sets the progress and starting point for the indeterminate progress animation.
     *
     * @param progress Current progress value.
     * @param startDegrees Current starting point.
     */
    private void setProgressStart(float progress, float startDegrees) {
        this.progress = progress;
        this.startingPoint = startDegrees;
        degrees = MAX_DEGREES * progress / maxProgress;
        invalidate();
    }

    /** Starts the indeterminate progress animation. */
    public void startAnimating() {
        if (indeterminate && !isAnimating) {
            isAnimating = true;
            animationHandler.sendEmptyMessage(0);
        }
    }

    /** Stops the indeterminate progress animation. */
    public void stopAnimating() {
        if (isAnimating) {
            animationHandler.removeMessages(0);
            progress = 0;
            reverse = false;
            startingPoint = startDegrees;
            setProgressStart(progress, startingPoint);
            isAnimating = false;
            invalidate();
        }
    }

    /** Handle the indeterminate progress animation */
    private void handleAnimation() {
        if (indeterminate) {
            if (progress >= maxProgress) {
                reverse = true;
                startingPoint = startDegrees;
                progress = maxProgress;
            } else if (progress <= 0) {
                reverse = false;
                startingPoint = startDegrees;
                progress = 0;
            }
            if (reverse) {
                float degrees1 = MAX_DEGREES * progress / maxProgress;
                progress -= animationStep;
                float degrees2 = MAX_DEGREES * progress / maxProgress;
                float diff = degrees1 - degrees2;
                startingPoint += diff;
            } else {
                progress += animationStep;
            }
            setProgressStart(progress, startingPoint);
            animationHandler.sendEmptyMessageDelayed(0, animationDelay);
        }
    }

    /** Handler class for handling the indeterminate progress animation */
    static class AnimationHandler extends Handler {
        private final WeakReference<ProgressButton> progressButtonReference;

        AnimationHandler(ProgressButton progressButton) {
            progressButtonReference = new WeakReference<>(progressButton);
        }

        @Override
        public void handleMessage(Message msg) {
            ProgressButton progressButton = progressButtonReference.get();
            if (progressButton != null) {
                progressButton.handleAnimation();
            }
        }
    }

}
