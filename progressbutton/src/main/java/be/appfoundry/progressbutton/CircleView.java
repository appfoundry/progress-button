package be.appfoundry.progressbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircleView extends View {

    private int color;
    private int strokeColor;
    private int progressColor;
    private float radius;
    private float stroke;
    private boolean indeterminate;

    private Paint circlePaint = new Paint();
    private Paint progressPaint = new Paint();
    private Paint strokePaint = new Paint();
    private Paint shadowPaint = new Paint();

    private float degrees;
    private float startDegrees = 270;

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
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
        return stroke;
    }

    public void setStroke(float stroke) {
        this.stroke = stroke;
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

    public float getStartDegrees() {
        return startDegrees;
    }

    public void setStartDegrees(float startDegrees) {
        this.startDegrees = startDegrees;
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

    private void init(Context context, AttributeSet attrs) {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        TypedArray attr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleView, 0, 0);

        try {
            color = attr.getColor(R.styleable.CircleView_innerColor, 0xFFFFFFFF);
            circlePaint.setColor(color);

            strokeColor = attr.getColor(R.styleable.CircleView_outerColor, 0xFFFFFFFF);
            strokePaint.setColor(strokeColor);

            progressColor = attr.getColor(R.styleable.CircleView_outerColor, 0xFFFFFFFF);
            progressPaint.setColor(progressColor);

            radius = attr.getInteger(R.styleable.CircleView_radius, 0);
        } finally {
            attr.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (radius == 0) radius = getWidth() / 2;

        RectF mTempRectF = new RectF();

        float left = (getWidth() / 2) - radius;
        float right = (getWidth() / 2) + radius;
        float top = (getHeight() / 2) - radius;
        float bottom = (getHeight() / 2) + radius;

        mTempRectF.set(left, top, right, bottom);

        canvas.drawCircle(getWidth() / 2, getWidth() / 2, radius, strokePaint);
        canvas.drawArc(mTempRectF, startDegrees, degrees, true, progressPaint);
        canvas.drawCircle(getWidth() / 2, getWidth() / 2, radius - stroke, circlePaint);
    }
}
