package com.drwang.views.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import static android.R.transition.move;

/**
 * Created by Administrator on 2017/7/28.
 */

public class SlideView extends View {

    private float density;
    private Paint paint;
    int measuredHeight;
    int measuredWidth;
    private int heightPixels;

    public SlideView(Context context) {
        this(context, null);

    }

    public SlideView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SlideView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        density = getResources().getDisplayMetrics().density;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setBackgroundColor(Color.BLUE);
        setAlpha(0.5f);
        heightPixels = getResources().getDisplayMetrics().heightPixels / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    float startX;
    float startY;
    float moveX;
    float moveY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startX == 0 || startY == 0) {
                    startX = event.getX();
                    startY = event.getY();
                    break;
                }
                moveX = event.getX();
                moveY = event.getY();
                if (moveX == 0 || moveY == 0){
                    break;
                }
                float xdelta = moveX - startX;
                float ydelta = moveY - startY;
                float v = 1f - ydelta / (heightPixels * 1.0f);
                setAlpha(v > 0.5f ? 0.5f : v);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                startX = 0;
                startY = 0;
                setAlpha(0.5f);
                moveX = 0;
                moveY = 0;
                break;
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        measuredWidth = w;
        measuredHeight = h;
    }

}

