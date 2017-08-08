package com.drwang.views.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by Administrator on 2017/7/17.
 */

public class PaintView extends View {

    private float density;
    private Paint paint;
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
        paint = new Paint();
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
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPath.lineTo(300, 300);
        //画圆弧
//        mPath.arcTo(300,300,500,500,0,90,true);
//        mPath.arcTo(new RectF(300, 300, 500, 500), 0, 90, true);
//        canvas.drawPath(mPath, mPaint);
//        mPaint.setColor(Color.RED);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setAntiAlias(true);
//        canvas.drawCircle(mWidth / 2, mHeight / 2, 50 * density, mPaint);
        //TileMode repeat 重复 MIRROR 镜像  CLAMP
        LinearGradient linearGradient = new LinearGradient(100, 100, 500, 500, Color.parseColor("#e91e63"), Color.parseColor("#2196F3"), Shader.TileMode.MIRROR);
        paint.setShader(linearGradient);
//        canvas.drawCircle(300,300,200,paint);
//        canvas.drawCircle(500,500,100,paint);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }
}
