package com.example.nathans.tooltip;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by nathans on 8/5/17.
 */

/**
 * Custom view to build a triangle.
 * Options: Border on all sides, border on two sides
 * Can make triangles like this ▲ or ▶. Combined with rotation in xml any triangle should be able to be made.
 * Methods adapted from https://stackoverflow.com/questions/2517589/making-a-triangle-shape-using-xml-definitions/39530948#39530948
 */
public class Triangle extends View {
    private static final int ORIENTATION_VERTICAL = 1;
    private static final int ORIENTATION_HORIZONTAL = 2;

    private int triangleOrientation;
    private int fillColor;
    private int strokeColor;
    private float strokeWidth;
    private boolean twoSidedStroke;

    public Triangle(Context context) {
        this(context, null);
    }

    public Triangle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Triangle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        loadAttrs(attrs, defStyle);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth() / 2;
        int h = getHeight() / 2;

        Path fillPath = getTrianglePath(w, h);
        fillPath.close();

        Paint fillPaint = new Paint();
        fillPaint.setColor(fillColor);
        fillPaint.setAntiAlias(true);

        canvas.drawPath(fillPath, fillPaint);

        if (strokeWidth != 0) {

            Path strokePath = twoSidedStroke ? getTwoSidedStroke(w, h) : getTrianglePath(w, h);
            strokePath.close();

            Paint strokePaint = new Paint();
            strokePaint.setStyle(Paint.Style.STROKE);
            strokePaint.setStrokeWidth(strokeWidth);
            strokePaint.setColor(strokeColor);

            canvas.drawPath(strokePath, strokePaint);
        }
    }

    /**
     * Return Path for triangle
     */
    private Path getTrianglePath(int w, int h) {
        Path path = new Path();
        if (isVertical()) {
            // ▲
            path.moveTo(0, 2 * h);
            path.lineTo(w, 0);
            path.lineTo(2 * w, 2 * h);
            path.lineTo(0, 2 * h);
        } else {
            // ▶
            path.moveTo(2 * w, 0);
            path.lineTo(0, h);
            path.lineTo(2 * w, 2 * h);
            path.lineTo(2 * w, 0);
        }
        return path;
    }

    /**
     * Return Path to stroke on 2 sides of a triangle
     */
    private Path getTwoSidedStroke(int w, int h) {
        Path path = new Path();
        if (isVertical()) {
            // ▲
            path.moveTo(0, 2 * h);
            path.lineTo(w, 0);
            path.moveTo(w, 0);
            path.lineTo(2 * w, 2 * h);
        } else {
            // ▶
            path.moveTo(2 * w, 0);
            path.lineTo(0, h);
            path.moveTo(0, h);
            path.lineTo(2 * w, 2 * h);
        }
        return path;
    }

    private boolean isVertical() {
        return triangleOrientation == ORIENTATION_VERTICAL;
    }

    private void loadAttrs(AttributeSet attrs, int defStyleAttr) {
        TypedArray a =
                getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.Triangle, defStyleAttr, 0);
        try {
            fillColor = a.getColor(R.styleable.Triangle_triangleColor, Color.BLACK);
            strokeColor = a.getColor(R.styleable.Triangle_triangleStrokeColor, -1);
            strokeWidth = a.getDimensionPixelOffset(R.styleable.Triangle_triangleStrokeWidth, 0); // convert to dp
            twoSidedStroke = a.getBoolean(R.styleable.Triangle_triangleTwoSidedStroke, false);
            triangleOrientation = a.getInt(R.styleable.Triangle_triangleOrientation, ORIENTATION_VERTICAL);
        } finally {
            a.recycle();
        }
    }
}
