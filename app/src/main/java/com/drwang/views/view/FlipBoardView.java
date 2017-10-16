package com.drwang.views.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.drwang.views.R;

/**
 * Created by wang on 2017/10/16.
 */

public class FlipBoardView extends View {
    Paint paint;
    Camera camera;
    Bitmap bitmap;
    private int degree;
    ObjectAnimator oa = ObjectAnimator.ofInt(this, "degree", 0, 360);

    public void setDegree(int degree) {
        this.degree = degree;
        invalidate();
    }

    public FlipBoardView(Context context) {
        this(context, null);

    }

    public FlipBoardView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlipBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        camera = new Camera();
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_bg);
        camera.setLocation(0, 0, 6 * getResources().getDisplayMetrics().density);
        oa.setInterpolator(new LinearInterpolator());
        oa.setDuration(1000);
        oa.setRepeatCount(ValueAnimator.INFINITE);
        oa.setRepeatMode(ValueAnimator.RESTART);
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
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int x = centerX - bitmap.getWidth() / 2;
        int y = centerY - bitmap.getHeight() / 2;

        //绘制下半部分
        canvas.save();
        canvas.translate(centerX, centerY);
        canvas.rotate(-degree);
        camera.save();
        camera.rotateX(-45);
        camera.applyToCanvas(canvas);
        camera.restore();
//        canvas.clipRect(0, centerY, getWidth(), getHeight());
        canvas.clipRect(-centerX, -centerY, centerX, centerY);
        canvas.rotate(degree);
        canvas.translate(-centerX, -centerY);
        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.restore();


        //绘制上半部分
        canvas.save();
        canvas.translate(centerX, centerY);
        canvas.rotate(-degree);
        camera.save();
        camera.applyToCanvas(canvas);
        camera.restore();
        canvas.clipRect(-centerX, -centerY, centerX, 0);
        canvas.rotate(degree);
        canvas.translate(-centerX, -centerY);
        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.restore();
    }
}
