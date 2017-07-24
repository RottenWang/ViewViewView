package com.drwang.views.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * Created by Administrator on 2017/7/21.
 */

public class ScanView extends View {

    private int measuredWidth;
    private int measuredHeight;
    private RectF rectF;
    private Rect rect;
    private Random random;
    private int startTime;
    OnZeroListener onZeroListener;
    float density;

    public ScanView(Context context) {
        super(context);
    }

    public ScanView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    Paint paint;


    {
        random = new Random();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setLayerType(LAYER_TYPE_HARDWARE, paint);
        drawDegree = 10;
        pointF = new PointF();
        rectF = new RectF();
        rect = new Rect();
        startTime = 10;
    }

    PointF pointF;
    float radius;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        density = getResources().getDisplayMetrics().density;
        paint.setStyle(Paint.Style.FILL);
        measuredWidth = getMeasuredWidth();
        measuredHeight = getMeasuredHeight();
        pointF.x = measuredWidth / 2;
        pointF.y = measuredHeight / 2;
        radius = measuredHeight > measuredWidth ? measuredWidth / 2.0f : measuredHeight / 2.0f;
        float left;
        float top;
        float right;
        float bottom;
        left = measuredHeight > measuredWidth ? 0 : (measuredWidth - measuredHeight) / 2.0f;
        top = measuredHeight > measuredWidth ? ((measuredHeight - measuredWidth) / 2.0f) : 0;
        right = measuredHeight > measuredWidth ? measuredWidth : measuredWidth - ((measuredWidth - measuredHeight) / 2.0f);
        bottom = measuredHeight > measuredWidth ? measuredHeight - ((measuredHeight - measuredWidth) / 2.0f) : measuredHeight;
        rectF.left = left;
        rectF.top = top;
        rectF.right = right;
        rectF.bottom = bottom;
    }

    float sweepAngle = 0;
    int colorCircle;
    int colorArc;
    boolean isFirst = true;
    float startAngle = 360;
    Handler handler = new Handler();
    float drawDegree;
    int textColor;
    int currentDegree;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int i = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        sweepAngle += drawDegree;
        currentDegree += drawDegree;
        currentDegree++;
        startAngle--;
        if (isFirst) {
            isFirst = false;
            colorCircle = Color.BLACK;
            colorArc = getRandomColor();
            textColor = getRandomColor();
            checkColor();
        }
        paint.setColor(colorCircle);
        if (currentDegree <= 360) {
            canvas.drawArc(rectF, startAngle + sweepAngle, 360 - sweepAngle, true, paint);
        }
        if (currentDegree >= 360) {
            startTime = --startTime < 0 ? 0 : startTime;
            if (startTime == 0 && onZeroListener != null) { //这个if里面的代码是在text 为 "0"的时候进行的操作,进行了停止绘制
                onZeroListener.onZero();
                paint.setColor(colorArc);
                canvas.drawArc(rectF, startAngle, 360, true, paint);
                drawText(canvas);
                canvas.restoreToCount(i);
                return;
            }
            sweepAngle = 0;
            currentDegree = 0;
            colorCircle = colorArc;
            paint.setColor(colorArc);
            canvas.drawArc(rectF, startAngle, 360, true, paint);
            colorArc = getRandomColor();
            textColor = getRandomColor();
            checkColor();
            startAngle = random.nextInt(360);
            drawText(canvas);
            canvas.restoreToCount(i);
            reDraw();
            return;
        }
        paint.setColor(colorArc);
        canvas.drawArc(rectF, startAngle, sweepAngle, true, paint);
        drawText(canvas);
        canvas.restoreToCount(i);
        reDraw();
    }

    private void drawText(Canvas canvas) {
        String time = String.valueOf(startTime);
        paint.setTextSize(radius / 2);
        paint.getTextBounds(time, 0, time.length(), rect);
        int right = -(rect.right + rect.left) / 2;
        int bottom = -(rect.bottom + rect.top) /2;
        float x = pointF.x + right;//161
        float y = pointF.y + bottom;//264
        paint.setColor(textColor);
        canvas.drawText(time, x, y, paint);
    }

    private void checkColor() {
        while (colorArc == colorCircle || colorArc == ignoreColor || textColor == colorArc) {
            colorArc = getRandomColor();
            textColor = getRandomColor();
        }
    }

    private void reDraw() {
        handler.postDelayed(runnable, (long) (1000 / (360 / (drawDegree + 1)) + 0.5f));
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            postInvalidate();
        }
    };

    //绘制的角度增量 建议在5左右 默认为5
    public void setMinDegree(int degree) {
        drawDegree = degree < 1 ? 1 : degree > 360 ? 360 : degree;
    }

    //设置忽略颜色
    public void setIgnoreColor(int color) {
        ignoreColor = color;
    }

    int ignoreColor = Color.WHITE;

    private int getRandomColor() {
        return Color.argb(random.nextInt(0xffffff), random.nextInt(0xffffff), random.nextInt(0xffffff), random.nextInt(0xffffff));
    }

    //设置倒计时开始的时间
    public void setTimeStart(int startTime) {
        this.startTime = startTime;
    }

    //当倒计时为0时.回调方法
    public void setOnZeroListener(OnZeroListener onZeroListener) {
        this.onZeroListener = onZeroListener;
    }

    public interface OnZeroListener {
        void onZero();
    }
}
