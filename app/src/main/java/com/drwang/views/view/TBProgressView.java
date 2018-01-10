package com.drwang.views.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.IntRange;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * 仿淘宝进度条
 * Created by wang on 2018/1/10.
 */

public class TBProgressView extends View {
    Paint paint;
    int pinkColor = Color.parseColor("#FFE4E4");
    int whiteColor = Color.parseColor("#FDF2F1");
    int foregroundColor = Color.parseColor("#FF382E");
    int foregroundColor1 = Color.parseColor("#FF5342");
    LinearGradient mLinearGradient;
    LinearGradient mForegroundLinearGradient;
    RectF rectF;
    float density;
    int[] bgColors;
    int[] foregroundColors;
    float[] positions;
    int progress = 100;
    private PorterDuffXfermode mSrcATopXfermode;
    private ObjectAnimator oa;

    public TBProgressView(Context context) {
        this(context, null);
    }

    public TBProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TBProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        density = Resources.getSystem().getDisplayMetrics().density;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(density);
        paint.setFilterBitmap(true);
        rectF = new RectF();
        bgColors = new int[]{whiteColor, pinkColor};
        foregroundColors = new int[]{foregroundColor1, foregroundColor};
        positions = new float[]{0.5f, 0.51f};
//        setBackgroundColor(Color.GRAY);
        mSrcATopXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP);
        oa = ObjectAnimator.ofInt(this, "progress", 0, 100);
        oa.setDuration(2000).setInterpolator(new LinearInterpolator());
        oa.setRepeatCount(ObjectAnimator.INFINITE);
        oa.setRepeatMode(ObjectAnimator.REVERSE);
    }

    /**
     * 设置进度 range 0~100
     *
     * @param progress
     */
    @Keep
    private void setProgress(@IntRange(from = 0, to = 100) int progress) {
        this.progress = progress;
        invalidate();
    }

    /**
     * 开始演示动画
     */
    public void startShowAnimate() {
        if (oa != null) {
            oa.start();
        }
    }

    /**
     * 停止演示动画
     */
    public void stopShowAnimate() {
        if (oa != null) {
            oa.cancel();
        }
    }

    /**
     * 设置进度背景颜色
     *
     * @param color1
     * @param color2
     */
    public void setBgColors(int color1, int color2) {
        bgColors[0] = color1;
        bgColors[1] = color2;
    }

    /**
     * 设置进度前景颜色
     *
     * @param color1
     * @param color2
     */
    public void setForegroundColors(int color1, int color2) {
        foregroundColors[0] = color1;
        foregroundColors[1] = color2;
    }

//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        startShowAnimate();
//    }
//
//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        stopShowAnimate();
//    }

    @Override

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingBottom() - getPaddingTop();
        if (mLinearGradient == null) {
            mLinearGradient = new LinearGradient(0, 0, width / 10, width / 10, bgColors, positions, Shader.TileMode.REPEAT);
        }
        if (mForegroundLinearGradient == null) {
            mForegroundLinearGradient = new LinearGradient(0, 0, width / 10, width / 10, foregroundColors, positions, Shader.TileMode.REPEAT);
        }
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setShader(mLinearGradient);
        rectF.set(0, 0, width, height);

        int i = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        canvas.translate(getPaddingLeft(), getPaddingTop());
        canvas.drawRoundRect(rectF, height / 2, height / 2, paint);
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(foregroundColor);
        canvas.drawRoundRect(rectF, height / 2, height / 2, paint);
        rectF.set(0, 0, width * progress / 100, height);
        paint.setXfermode(mSrcATopXfermode);
        paint.setStyle(Paint.Style.FILL);
        paint.setShader(mForegroundLinearGradient);
        canvas.drawRoundRect(rectF, height / 2, height / 2, paint);
        canvas.translate(-getPaddingLeft(), -getPaddingTop());
        canvas.restoreToCount(i);
        paint.setXfermode(null);
        paint.setShader(null);
    }


}
