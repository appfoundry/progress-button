package be.appfoundry.progressbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import be.appfoundry.progressbutton.util.LayoutUtil;

public class ProgressButton extends View {

    private int color;
    private int strokeColor;
    private int progressColor;
    private float strokeWidth;
    private float maxProgress = 100f;
    private float animationStep = 1.0f;
    private boolean isIndeterminate = true;
    private boolean reverse = false;
    private Drawable icon;
    private float radius;

    private float progress;
    private boolean isAnimating = false;
    private float startDegrees = 270;
    private int animationDelay = 0;

    private boolean indeterminate;

    private Paint circlePaint = new Paint();
    private Paint progressPaint = new Paint();
    private Paint strokePaint = new Paint();

    private float degrees;

    private Handler animationHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
            if (isAnimating) {
                handleAnimation();
            }
        }
    };

    RectF buttonRectF = new RectF();
    Rect iconRect = new Rect();


    public ProgressButton(Context context) {
        this(context,null);
        init(context, null);
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        init(context, attrs);
    }

    public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        TypedArray attr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ProgressButton, 0, 0);

        try {
            color = attr.getColor(R.styleable.ProgressButton_fillColor, 0xFFFFFFFF);
            circlePaint.setColor(color);

            strokeColor = attr.getColor(R.styleable.ProgressButton_strokeColor, 0xFFFFFFFF);
            strokePaint.setColor(strokeColor);

            progressColor = attr.getColor(R.styleable.ProgressButton_progressColor, 0xFFFFFFFF);
            progressPaint.setColor(progressColor);

            strokeWidth = attr.getDimension(R.styleable.ProgressButton_strokeWidth, 0f);

            isIndeterminate = attr.getBoolean(R.styleable.ProgressButton_indeterminate, true);

            icon = attr.getDrawable(R.styleable.ProgressButton_progressIcon);
        } finally {
            attr.recycle();
        }
    }

    public float getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(float maxProgress) {
        this.maxProgress = maxProgress;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        degrees = 360 * progress / maxProgress;
        invalidate();
    }

    public float getStartDegrees() {
        return startDegrees;
    }

    public void setStartDegrees(float degrees) {
        this.startDegrees = degrees;
        invalidate();
    }

    public void setProgressStart(float progress, float startDegrees) {
        this.progress = progress;
        this.startDegrees = startDegrees;
        degrees = 360 * progress / maxProgress;
        invalidate();
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
        invalidate();
    }

    public float getAnimationStep() {
        return animationStep;
    }

    public void setAnimationStep(float animationStep) {
        this.animationStep = animationStep;
    }

    public void setAnimationDelay(int animationDelay) {
        this.animationDelay = animationDelay;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        invalidate();
        requestLayout();
    }

    public float getStroke() {
        return strokeWidth;
    }

    public void setStroke(float stroke) {
        this.strokeWidth = stroke;
        invalidate();
        requestLayout();
    }

    public float getDegrees() {
        return degrees;
    }

    public void setDegrees(float degrees) {
        this.degrees = degrees;
        invalidate();
        requestLayout();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        circlePaint.setColor(color);
        invalidate();
        requestLayout();
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        strokePaint.setColor(strokeColor);
        invalidate();
        requestLayout();
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        progressPaint.setColor(progressColor);
        invalidate();
        requestLayout();
    }

    public boolean isIndeterminate() {
        return indeterminate;
    }

    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
        invalidate();
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);

        int width = getLayoutParams().width;
        int height = getLayoutParams().height;

        int get_width = getWidth();
        int get_height = getHeight();

        int resolved_width = resolveSize(get_width, widthMeasureSpec);
        int resolved_height = resolveSize(get_height, heightMeasureSpec);

        int size;

        if (radius > 0) {
            size = (int) (radius * 2);
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.MATCH_PARENT && getLayoutParams().height == ViewGroup.LayoutParams.MATCH_PARENT) {
            size = resolved_width;
            if (size > resolved_height) size = resolved_height;
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.MATCH_PARENT && getLayoutParams().height != ViewGroup.LayoutParams.MATCH_PARENT) {
            size = resolved_height;
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.MATCH_PARENT && getLayoutParams().width != ViewGroup.LayoutParams.MATCH_PARENT) {
            size = resolved_width;
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            size = (int) LayoutUtil.convertDpToPixel(this.getContext(), 48);
        } else if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().height != ViewGroup.LayoutParams.WRAP_CONTENT) {
            size = resolved_height;
        } else if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().width != ViewGroup.LayoutParams.WRAP_CONTENT) {
            size = resolved_width;
        } else if (width > height) {
            size = resolved_height;
        } else if (width < height) {
            size = resolved_width;
        } else {
            if (resolved_height == 0) {
                size = width;
                if (size > resolved_width) size = resolved_width;
            } else if (resolved_width == 0) {
                size = height;
                if (size > resolved_height) size = resolved_height;
            } else {
                size = resolved_width;
                if (size > resolved_height) size = resolved_height;
            }
        }

        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (radius == 0) radius = getWidth() / 2;

        float left = (getWidth() / 2) - radius;
        float right = (getWidth() / 2) + radius;
        float top = (getHeight() / 2) - radius;
        float bottom = (getHeight() / 2) + radius;

        buttonRectF.set(left, top, right, bottom);

        canvas.drawCircle(getWidth() / 2, getWidth() / 2, radius, strokePaint);
        canvas.drawArc(buttonRectF, startDegrees, degrees, true, progressPaint);
        canvas.drawCircle(getWidth() / 2, getWidth() / 2, radius - strokeWidth, circlePaint);

        if (icon != null) {
            iconRect.set(0, 0, (int)radius, (int)radius);
            iconRect.offset((getWidth() - (int)radius) / 2, (getHeight() - (int)radius) / 2);
            icon.setBounds(iconRect);
            icon.draw(canvas);
        }
    }

    public void startAnimating() {
        if (!isAnimating) {
            isAnimating = true;
            animationHandler.sendEmptyMessage(0);
        }
    }

    public void stopAnimating() {
        if (isAnimating) {
            animationHandler.removeMessages(0);
            progress = 0;
            reverse = false;
            startDegrees = 270;
            setProgressStart(progress, startDegrees);
            isAnimating = false;
            invalidate();
        }
    }

    private void handleAnimation() {
        if (isIndeterminate) {
            if (progress >= maxProgress) {
                reverse = true;
                progress = maxProgress;
                startDegrees = 270;
            } else if (progress <= 0) {
                reverse = false;
                progress = 0;
                startDegrees = 270;
            }
            if (reverse) {
                float degrees1 = 360 * progress / maxProgress;
                progress -= animationStep;
                float degrees2 = 360 * progress / maxProgress;
                float diff = degrees1 - degrees2;
                startDegrees += diff;
            } else {
                progress += animationStep;
            }
        }
        setProgressStart(progress, startDegrees);
        animationHandler.sendEmptyMessageDelayed(0, animationDelay);
    }

}
