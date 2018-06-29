package com.drwang.views.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class CDView extends View {
    private Paint paint;
    private int width;
    private int originalWidth;
    private int originalHeight;
    ObjectAnimator oa;
    private SweepGradient mSweepGradient;
    int[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED};
    float[] positions = {0.5f, 0.25f, 0.25f, 0.25f, 0.5f};

    public CDView(Context context) {
        this(context, null);
    }

    public CDView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CDView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        oa = ObjectAnimator.ofFloat(this,"rotation",0f,360f);
        oa.setInterpolator(new LinearInterpolator());
        oa.setRepeatCount(ValueAnimator.INFINITE);
        oa.setRepeatMode(ValueAnimator.RESTART);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        originalWidth = w;
        originalHeight = h;
        if (w > h) {
            w = h;
        } else {
            h = w;
        }

        width = w;
        mSweepGradient = new SweepGradient(originalWidth / 2, originalHeight / 2, colors, null);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        oa.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        oa.cancel();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = width / 2;
        int cx = originalWidth / 2;
        int cy = originalHeight / 2;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GRAY);
        canvas.drawCircle(cx, cy, radius, paint);
        if (mSweepGradient == null) {
            mSweepGradient = new SweepGradient(originalWidth / 2, originalHeight / 2, colors, null);
        }
        paint.setShader(mSweepGradient);
        canvas.drawCircle(cx, cy, 9 * radius / 10, paint);
        paint.setShader(null);
        paint.setColor(Color.BLACK);
        canvas.drawCircle(cx, cy, 4 * radius / 10, paint);
        paint.setColor(Color.GRAY);
        canvas.drawCircle(cx, cy, 3 *radius / 10, paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(cx, cy, 2 * radius / 10, paint);

    }
}
