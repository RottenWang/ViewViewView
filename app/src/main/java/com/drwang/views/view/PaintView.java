package com.drwang.views.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import static android.R.attr.path;


/**
 * Created by Administrator on 2017/7/17.
 */

public class PaintView extends View {

    private float density;
    private Paint mPaint;
    private int mHeight;
    private int mWidth;
    private Path mPath;

    public PaintView(Context context) {
        this(context, null);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PaintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        density = getResources().getDisplayMetrics().density;
        mPaint = new Paint();
        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 心形
//        mPath.addArc(200, 200, 400, 400, -225, 225);
//        mPath.arcTo(400, 200, 600, 400, -180, 225, false);
//        mPath.lineTo(400, 542);
        //圆
//        mPath.addCircle(300,300,300, Path.Direction.CW);
        //贝塞尔曲线
//        mPath.moveTo(500, 100);
//        mPath.quadTo(300,400,100,500);
//        mPath.quadTo(300,200,500,100);
//        mPath.cubicTo(400,200,600,300,500,500);

//        mPath.lineTo(400, 500);
        mPaint.setStyle(Paint.Style.STROKE);
        mPath.lineTo(300, 300);
        //画圆弧
//        mPath.arcTo(300,300,500,500,0,90,true);
        mPath.arcTo(new RectF(300, 300, 500, 500), 0, 90, true);
        canvas.drawPath(mPath, mPaint);
//        mPaint.setColor(Color.RED);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setAntiAlias(true);
//        canvas.drawCircle(mWidth / 2, mHeight / 2, 50 * density, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }
}
