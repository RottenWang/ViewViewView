package com.drwang.views.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wang on 2017/10/9.
 * //通过Scroller 来平移
 */

public class SelectedMoneyView extends View {

    private Paint paint;
    private float density;
    private int deltaX;
    private int scrollerXDefault;
    private float totalWidth;
    private int w;
    private int animateX;
    private int ignoredRange;


    public SelectedMoneyView(Context context) {
        this(context, null);
    }

    public SelectedMoneyView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectedMoneyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        density = getResources().getDisplayMetrics().density;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(density);
        paint.setTextSize(density * 16);
        deltaX = (int) (15 * density + 0.5f);
        totalWidth = deltaX * 30;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        scrollerXDefault = (int) ((totalWidth - w) / 2 + 0.5f);
        scrollTo(scrollerXDefault, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        float lineShort = density * 10;
        float lineLong = density * 20;
        float startX = 0;
        float startY;
        //画金额的线
        for (int i = 0; i < 31; i++) {
            if (i % 5 == 0) {
                startY = height - lineLong;
                drawText(i, canvas, startX, startY - 10 * density);
            } else {
                startY = height - lineShort;
            }

            canvas.drawLine(startX, startY, startX, height, paint);
            startX += deltaX;
        }
    }

    @Override
    public void onDrawForeground(Canvas canvas) {
        super.onDrawForeground(canvas);
        canvas.drawText("hahahahahaha", 0, 100, paint);
    }

    private void drawText(int i, Canvas canvas, float x, float y) {
        String text = String.valueOf(i * 100);
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect); //获取文字边界
        float lineX = x - (rect.left + rect.right) / 2.0f; // 计算文字的基准线
        float lineY = y - (rect.top + rect.bottom) / 2.0f;
        //画文字
        canvas.drawText(text, lineX, lineY, paint);
    }

    int startX;
    int moveX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                startX = (int) (event.getX() + 0.5f);
                break;
            case MotionEvent.ACTION_MOVE:
                if (startX == 0) {
                    startX = (int) (event.getX() + 0.5f);
                    return true;
                }
                moveX = (int) (event.getX() + 0.5f);
                int deltaX = moveX - startX;
                startX = moveX;
                scrollBy(-deltaX, 0);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                //回到最近的刻度
                int scrollX = getScrollX();
                if (scrollX <= scrollerXDefault) {
                    animToLocation(scrollX, 0);
                    scrollTo(scrollerXDefault, 0);
                }
                break;
        }
        return true;
    }

    private void animToLocation(int start, int end) {
        ObjectAnimator animateX = ObjectAnimator.ofInt(this, "animateX", start, end);
        animateX.start();
    }


    public void setAnimateX(int animateX) {
        this.animateX = animateX;
        scrollTo(animateX, 0);
    }


    public void setIgnoredRange(int width) {
        this.ignoredRange = width;
    }
}
