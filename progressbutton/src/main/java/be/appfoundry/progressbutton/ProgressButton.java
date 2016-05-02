package be.appfoundry.progressbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import be.appfoundry.progressbutton.util.LayoutUtil;

public class ProgressButton extends FrameLayout {

    private CircleView circleView;
    private ImageView imageView;

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

    private Handler animationHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
            if (isAnimating) {
                handleAnimation();
            }
        }
    };


    public ProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.progress_button, this, true);
        circleView = (CircleView) view.findViewById(R.id.circle_view);

        imageView = (ImageView) findViewById(R.id.icon);

        TypedArray attr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ProgressButton, 0, 0);

        try {
            color = attr.getColor(R.styleable.ProgressButton_fillColor, 0xFFFFFFFF);
            circleView.setColor(color);
            strokeColor = attr.getColor(R.styleable.ProgressButton_strokeColor, 0xFFFFFFFF);
            circleView.setStrokeColor(strokeColor);
            progressColor = attr.getColor(R.styleable.ProgressButton_progressColor, 0xFFFFFFFF);
            circleView.setProgressColor(progressColor);

            strokeWidth = attr.getDimension(R.styleable.ProgressButton_strokeWidth, 0f);
            circleView.setStroke(strokeWidth);

            isIndeterminate = attr.getBoolean(R.styleable.ProgressButton_indeterminate, true);
            circleView.setIndeterminate(isIndeterminate);

            icon = attr.getDrawable(R.styleable.ProgressButton_progressIcon);
            setIcon(icon);
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
        float degrees = 360 * progress / maxProgress;
        circleView.setDegrees(degrees);
        invalidate();
    }

    public void setStartDegrees(float degrees) {
        this.startDegrees = degrees;
        circleView.setStartDegrees(degrees);
        invalidate();
    }

    public void setProgressStart(float progress, float startDegrees) {
        this.progress = progress;
        this.startDegrees = startDegrees;
        circleView.setStartDegrees(startDegrees);
        float degrees = 360 * progress / maxProgress;
        circleView.setDegrees(degrees);
        invalidate();
    }

    public boolean isIndeterminate() {
        return isIndeterminate;
    }

    public void setIndeterminate(boolean indeterminate) {
        isIndeterminate = indeterminate;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
        imageView.setBackground(icon);
        invalidate();
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        circleView.setRadius(radius);
        invalidate();
    }

    public void setAnimationStep(float animationStep) {
        this.animationStep = animationStep;
    }

    public void setAnimationDelay(int animationDelay) {
        this.animationDelay = animationDelay;
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
        } else if (getLayoutParams().width == LayoutParams.MATCH_PARENT && getLayoutParams().height == LayoutParams.MATCH_PARENT) {
            size = resolved_width;
            if (size > resolved_height) size = resolved_height;
        } else if (getLayoutParams().width == LayoutParams.MATCH_PARENT && getLayoutParams().height != LayoutParams.MATCH_PARENT) {
            size = resolved_height;
        } else if (getLayoutParams().height == LayoutParams.MATCH_PARENT && getLayoutParams().width != LayoutParams.MATCH_PARENT) {
            size = resolved_width;
        } else if (getLayoutParams().width == LayoutParams.WRAP_CONTENT && getLayoutParams().height == LayoutParams.WRAP_CONTENT) {
            size = (int) LayoutUtil.convertDpToPixel(this.getContext(), 48);
        } else if (getLayoutParams().width == LayoutParams.WRAP_CONTENT && getLayoutParams().height != LayoutParams.WRAP_CONTENT) {
            size = resolved_height;
        } else if (getLayoutParams().height == LayoutParams.WRAP_CONTENT && getLayoutParams().width != LayoutParams.WRAP_CONTENT) {
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
        if (radius == 0) circleView.setRadius(getWidth() / 2);
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
