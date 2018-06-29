package com.drwang.views.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.Random;

public class PentagonView extends View {
    private Paint paint;
    private float radius;
    private int width;
    private int height;
    private PointF pointF_Top;
    private PointF pointF_TopRight;
    private PointF pointF_BottomRight;
    private PointF pointF_TopLeft;
    private PointF pointF_BottomLeft;
    Path path;
    float percentTop = 0.76f;
    float percentTopRight = 0.4f;
    float percentBottomRight = 0.9f;
    float percentTopLeft = 0.2f;
    float percentBottomLeft = 0.8f;
    float percentAll = 1f;
    private ObjectAnimator oa;
    private boolean isAnimated = false;

    public void setPercents(float percentTop, float percentTopRight, float percentBottomRight, float percentTopLeft, float percentBottomLeft) {
        this.percentTop = percentTop < 0 ? 0 : percentTop > 1 ? 1 : percentTop;
        this.percentTopRight = percentTopRight < 0 ? 0 : percentTopRight > 1 ? 1 : percentTopRight;
        this.percentBottomRight = percentBottomRight < 0 ? 0 : percentBottomRight > 1 ? 1 : percentBottomRight;
        this.percentTopLeft = percentTopLeft < 0 ? 0 : percentTopLeft > 1 ? 1 : percentTopLeft;
        this.percentBottomLeft = percentBottomLeft < 0 ? 0 : percentBottomLeft > 1 ? 1 : percentBottomLeft;
        invalidate();
        oa.start();
    }

    @Keep
    private void setPercentAll(float percentAll) {
        this.percentAll = percentAll;
        invalidate();
    }

    public PentagonView(Context context) {
        this(context, null);
    }

    public PentagonView(Context context, @Nullable AttributeSet attrs) {
        this(context, null, 0);
    }

    public PentagonView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        Random r = new Random();
        percentTop = r.nextFloat();
        percentTopRight = r.nextFloat();
        percentBottomRight = r.nextFloat();
        percentTopLeft = r.nextFloat();
        percentBottomLeft = r.nextFloat();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        path = new Path();
        oa = ObjectAnimator.ofFloat(this, "percentAll", 0f, 1f);
        oa.setInterpolator(new LinearInterpolator());
        oa.setDuration(500);
        oa.setRepeatCount(0);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isAnimated) {
            isAnimated = true;
            oa.start();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        if (w > h) {
            w = h;
        } else {
            h = w;
        }
        radius = 0.8f * h / 2;
    }

    private void initPoints(float radius) {
        initTopPointF(radius);
        initTopRightPointF(radius);
        initBottomRightPointF(radius);
        initBottomLeftPointF(radius);
        initTopLeftPointF(radius);
    }

    private void initPercentPoints() {
        initTopPointF(radius * percentTop * percentAll);
        initTopRightPointF(radius * percentTopRight * percentAll);
        initBottomRightPointF(radius * percentBottomRight * percentAll);
        initBottomLeftPointF(radius * percentBottomLeft * percentAll);
        initTopLeftPointF(radius * percentTopLeft * percentAll);
    }

    private void initTopPointF(float radius) {
        pointF_Top = new PointF(width / 2, height / 2 - radius);
    }

    private void initTopRightPointF(float radius) {
        float sin72 = (float) (radius * Math.sin(Math.toRadians(72)));
        float cos72 = (float) (radius * Math.cos(Math.toRadians(72)));
        pointF_TopRight = new PointF(width / 2 + sin72, height / 2 - cos72);
    }

    private void initBottomRightPointF(float radius) {
        float cos54 = (float) (radius * Math.cos(Math.toRadians(54)));
        float sin54 = (float) (radius * Math.sin(Math.toRadians(54)));
        pointF_BottomRight = new PointF(width / 2 + cos54, height / 2 + sin54);
    }

    private void initBottomLeftPointF(float radius) {
        float sin36 = (float) (radius * Math.sin(Math.toRadians(36)));
        float cos36 = (float) (radius * Math.cos(Math.toRadians(36)));
        pointF_BottomLeft = new PointF(width / 2 - sin36, height / 2 + cos36);
    }

    private void initTopLeftPointF(float radius) {
        float sin72 = (float) (radius * Math.sin(Math.toRadians(72)));
        float cos72 = (float) (radius * Math.cos(Math.toRadians(72)));
        pointF_TopLeft = new PointF(width / 2 - sin72, height / 2 - cos72);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.GRAY);
        drawOutlines(canvas);
        drawLines(canvas);
        drawCenterGraphics(canvas);
    }

    private void drawCenterGraphics(Canvas canvas) {
        initPercentPoints();
        paint.reset();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAlpha(100);
        path.reset();
        path.moveTo(pointF_Top.x, pointF_Top.y);
        path.lineTo(pointF_TopRight.x, pointF_TopRight.y);
        path.lineTo(pointF_BottomRight.x, pointF_BottomRight.y);
        path.lineTo(pointF_BottomLeft.x, pointF_BottomLeft.y);
        path.lineTo(pointF_TopLeft.x, pointF_TopLeft.y);
        path.lineTo(pointF_Top.x, pointF_Top.y);
        canvas.drawPath(path, paint);
    }


    private void drawOutlines(Canvas canvas) {
        float localRadius;
        for (int i = 0; i < 5; i++) {
            localRadius = radius * (5 - i) / 5f;
            initPoints(localRadius);
            drawOutLine(canvas);
        }


    }

    private void drawOutLine(Canvas canvas) {
        paint.reset();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        path.reset();
        path.moveTo(pointF_Top.x, pointF_Top.y);
        path.lineTo(pointF_TopRight.x, pointF_TopRight.y);
        path.lineTo(pointF_BottomRight.x, pointF_BottomRight.y);
        path.lineTo(pointF_BottomLeft.x, pointF_BottomLeft.y);
        path.lineTo(pointF_TopLeft.x, pointF_TopLeft.y);
        path.lineTo(pointF_Top.x, pointF_Top.y);
        canvas.drawPath(path, paint);
    }

    private void drawLines(Canvas canvas) {
        initPoints(radius);
        canvas.drawLine(width / 2, height / 2, pointF_Top.x, pointF_Top.y, paint);
        canvas.drawLine(width / 2, height / 2, pointF_TopRight.x, pointF_TopRight.y, paint);
        canvas.drawLine(width / 2, height / 2, pointF_BottomRight.x, pointF_BottomRight.y, paint);
        canvas.drawLine(width / 2, height / 2, pointF_BottomLeft.x, pointF_BottomLeft.y, paint);
        canvas.drawLine(width / 2, height / 2, pointF_TopLeft.x, pointF_TopLeft.y, paint);
    }
}
