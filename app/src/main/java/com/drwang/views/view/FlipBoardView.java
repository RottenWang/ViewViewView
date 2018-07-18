package com.drwang.views.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
    private int startDegree = 0;

    private int degree;
    ObjectAnimator oa = ObjectAnimator.ofInt(this, "degree", startDegree, startDegree + 270);
    ObjectAnimator ox = ObjectAnimator.ofInt(this, "degreeX", 0, 45);
    ObjectAnimator ox2 = ObjectAnimator.ofInt(this, "degreeX2", 0, 45);
    private int degreeX;
    private int degreeX2;

    public void setDegree(int degree) {
        this.degree = degree;
        invalidate();
    }

    public void setStartDegree(int degree) {
        startDegree = degree;
        oa.setIntValues(startDegree, startDegree + 270);
//        resetDegree();
    }

    private void resetDegree() {
        ox.cancel();
        oa.cancel();
        ox2.cancel();
        degreeX2 = 0;
        degreeX = 0;
        degree = startDegree;
//        ox.start();
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        resetDegree();
    }


    public void setDegreeX(int degreeX) {
        this.degreeX = degreeX;
        invalidate();
    }

    public void setDegreeX2(int degreeX2) {
        this.degreeX2 = degreeX2;
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
        oa.setDuration(800);
        ox.setInterpolator(new LinearInterpolator());
        ox.setDuration(500);
        ox.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                postDelayed(() -> {
                    oa.start();

                }, 200);
            }
        });
        oa.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
//                ox2.start();
                postDelayed(() -> {
                    ox2.start();

                }, 200);
            }
        });
        ox2.setDuration(500);
        ox2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                postDelayed(() -> {
                    resetDegree();
                    ox.start();
                }, 200);

            }
        });
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ox.cancel();
        oa.cancel();
        ox2.cancel();
        ox.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ox.cancel();
        oa.cancel();
        ox2.cancel();
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
        camera.rotateX(-degreeX);
        camera.applyToCanvas(canvas);
        camera.restore();
//        canvas.clipRect(0, centerY, getWidth(), getHeight());
        //裁剪区域大一点 防止非正方形旋转时 由于裁剪区域过小而造成的部分图像缺失问题
        canvas.clipRect(-centerX, 0, centerX, centerY * 2);
        canvas.rotate(degree);
        canvas.translate(-centerX, -centerY);
        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.restore();


        //绘制上半部分
        canvas.save();
        canvas.translate(centerX, centerY);
        canvas.rotate(-degree);
        camera.save();
        camera.rotateX(degreeX2);
        camera.applyToCanvas(canvas);
        camera.restore();
        canvas.clipRect(-centerX, -centerY * 2, centerX, 0);
        canvas.rotate(degree);
        canvas.translate(-centerX, -centerY);
        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.restore();
    }
}
