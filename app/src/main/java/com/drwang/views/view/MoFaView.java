package com.drwang.views.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.drwang.views.R;

/**
 * Created by Administrator on 2017/7/25.
 */

public class MoFaView extends View {

    int measuredWidth;
    int measuredHeight;
    Paint paint;
    int colorBlack;
    float density;
    Path path;
    Path path2;
    Path path3;
    Path path4;
    PorterDuffXfermode porterDuffXfermode;
    private float degree;
    int wh;
    private String[] directionArr = {"西", "北", "東", "南"};

    public MoFaView(Context context) {
        super(context);
        init();
    }


    public MoFaView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MoFaView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setBackgroundColor(Color.TRANSPARENT);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        colorBlack = getResources().getColor(R.color.colorAccent);
        paint.setColor(colorBlack);
        density = getResources().getDisplayMetrics().density;
        path = new Path();
        path2 = new Path();
        path3 = new Path();
        path4 = new Path();
        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.XOR);
        setLayerType(LAYER_TYPE_SOFTWARE, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        measuredWidth = w;
        measuredHeight = h;
        measuredWidth = measuredWidth > measuredHeight ? measuredHeight : measuredWidth;
        measuredHeight = measuredWidth > measuredHeight ? measuredHeight : measuredWidth;
        wh = Math.abs(measuredWidth - measuredHeight);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setShadowLayer(10 * density, 0, 0, getResources().getColor(R.color.colorAccent));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(density);
        //画圆
        path.addCircle(measuredWidth / 2, measuredHeight / 2, measuredWidth / 2 - density * 10, Path.Direction.CCW);
        path.addCircle(measuredWidth / 2, measuredHeight / 2, measuredWidth / 2 - density * 17, Path.Direction.CCW);
        canvas.drawPath(path, paint);
        paint.setStrokeWidth(0);
        path2.addCircle(measuredWidth / 2, measuredHeight / 2, measuredWidth / 2 - density * 22, Path.Direction.CCW);
        path2.addCircle(measuredWidth / 2, measuredHeight / 2, measuredWidth / 2 - density * 25, Path.Direction.CCW);
        path2.addCircle(measuredWidth / 2, measuredHeight / 2, measuredWidth / 2 - density * 34, Path.Direction.CCW);
        path2.addCircle(measuredWidth / 2, measuredHeight / 2, measuredWidth / 2 - density * 37, Path.Direction.CCW);
        path2.addCircle(measuredWidth / 2, measuredHeight / 2, measuredWidth / 2 - density * 84, Path.Direction.CCW);
        path2.addCircle(measuredWidth / 2, measuredHeight / 2, measuredWidth / 2 - density * 86, Path.Direction.CCW);
        path2.addCircle(measuredWidth / 2, measuredHeight / 2, measuredWidth / 2 - density * 143, Path.Direction.CCW);
        path2.addCircle(measuredWidth / 2, measuredHeight / 2, measuredWidth / 2 - density * 145, Path.Direction.CCW);
        canvas.drawPath(path2, paint);
        //矩形的一半的距离
        float v = measuredWidth / 2 - density * 84;
        float half = measuredWidth / 2;
        paint.setStrokeWidth(1 * density);
        paint.setStrokeJoin(Paint.Join.MITER);
        int i = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        paint.setSubpixelText(true);
        float detal = 5 * density;
        //画6个矩形
        canvas.rotate(45, measuredWidth / 2, measuredHeight / 2);
        canvas.drawRect(half - v, half - v, half + v, half + v, paint);
        canvas.drawRect(-detal + half - v, -detal + half - v, detal + half + v, detal + half + v, paint);
        canvas.rotate(-30f, measuredWidth / 2, measuredHeight / 2);
        paint.setXfermode(porterDuffXfermode);
        canvas.drawRect(half - v, half - v, half + v, half + v, paint);
        canvas.drawRect(-detal + half - v, -detal + half - v, detal + half + v, detal + half + v, paint);
        canvas.rotate(60, measuredWidth / 2, measuredHeight / 2);
        canvas.drawRect(half - v, half - v, half + v, half + v, paint);
        canvas.drawRect(-detal + half - v, -detal + half - v, detal + half + v, detal + half + v, paint);
        paint.setXfermode(null);
        canvas.restoreToCount(i);
        paint.setStrokeWidth(0);
        canvas.save();
        canvas.rotate(-0.5f, measuredWidth / 2, measuredHeight / 2);
        degree += 0.5;
        //画短线
        while (degree <= 360) {
            canvas.drawLine(density * 25, measuredHeight / 2, density * 34, measuredHeight / 2, paint);
            degree++;
            canvas.rotate(1, measuredWidth / 2, measuredHeight / 2);
            canvas.drawLine(density * 25, measuredHeight / 2, density * 34, measuredHeight / 2, paint);
            degree += 14;
            canvas.rotate(14, measuredWidth / 2, measuredHeight / 2);
        }
        canvas.restore();
        canvas.save();
        degree = 0;
        canvas.rotate(-5.5f, measuredWidth / 2, measuredHeight / 2);
        degree += 5.5f;
        int drawCount = 0;
        float delta2 = (float) (21 * density / 2 * Math.tan(Math.toRadians(38)));
        //画长线
        while (degree <= 360) {
            drawCount++;
            if (drawCount % 6 == 1) {
                canvas.drawLine(density * 86, measuredHeight / 2 - delta2, density * 107, measuredHeight / 2, paint);
                canvas.rotate(5, measuredWidth / 2, measuredHeight / 2);
                canvas.drawLine(density * 86, measuredHeight / 2, density * 107, measuredHeight / 2, paint);
                canvas.rotate(1, measuredWidth / 2, measuredHeight / 2);
                canvas.drawLine(density * 86, measuredHeight / 2, density * 107, measuredHeight / 2, paint);
                canvas.rotate(-6, measuredWidth / 2, measuredHeight / 2);
            }
            if (drawCount % 6 == 2) {
                canvas.drawLine(density * 86, measuredHeight / 2 + delta2, density * 107, measuredHeight / 2, paint);
            }
            if (drawCount % 3 == 0) {
                canvas.drawLine(density * 86, measuredHeight / 2, density * 143, measuredHeight / 2, paint);
            } else {
                canvas.drawLine(density * 97.5f, measuredHeight / 2, density * 143, measuredHeight / 2, paint);
            }
            degree++;
            canvas.rotate(1, measuredWidth / 2, measuredHeight / 2);
            if (drawCount % 6 == 1) {
                canvas.drawLine(density * 86, measuredHeight / 2 - delta2, density * 107, measuredHeight / 2, paint);
            }
            if (drawCount % 6 == 2) {
                canvas.drawLine(density * 86, measuredHeight / 2 + delta2, density * 107, measuredHeight / 2, paint);
            }
            if (drawCount != 0 && drawCount % 3 == 0) {
                canvas.drawLine(density * 86, measuredHeight / 2, density * 143, measuredHeight / 2, paint);
            } else {
                canvas.drawLine(density * 97.5f, measuredHeight / 2, density * 143, measuredHeight / 2, paint);
            }
            degree += 9;
            canvas.rotate(9, measuredWidth / 2, measuredHeight / 2);
        }
        canvas.restore();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        //画嵌套的圆
        path3.addCircle(measuredWidth / 2, measuredHeight / 2, measuredWidth / 2 - density * 107, Path.Direction.CCW);
        path4.addCircle(measuredWidth / 2 - 10 * density, measuredHeight / 2 + 10 * density, measuredWidth / 2 - density * 121, Path.Direction.CCW);
        path3.op(path4, Path.Op.DIFFERENCE);
        canvas.drawPath(path3, paint);
        paint.setColor(colorBlack);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(density);
        canvas.drawCircle(measuredWidth / 2, measuredHeight / 2, measuredWidth / 2 - density * 107, paint);
        canvas.drawCircle(measuredWidth / 2 - 10 * density, measuredHeight / 2 + 10 * density, measuredWidth / 2 - density * 121, paint);
        for (int j = 0; j < 4; j++) {
            drawcircle(canvas, paint, directionArr[j]);
        }
        //画嵌套的圆
        path3.reset();
        path4.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        path3.addCircle(282 * density, measuredHeight / 2, 68 * density, Path.Direction.CCW);
        path4.addCircle(282 * density, measuredHeight / 2, 63 * density, Path.Direction.CCW);
        path3.op(path4, Path.Op.DIFFERENCE);
        canvas.drawPath(path3, paint);
        paint.setColor(colorBlack);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(density);
        canvas.drawCircle(282 * density, measuredHeight / 2, 68 * density, paint);
        canvas.drawCircle(282 * density, measuredHeight / 2, 63 * density, paint);
        //画最后一个嵌套圆
        path3.reset();
        path4.reset();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        path3.addCircle(287 * density, measuredHeight / 2 - 22 * density, 40 * density, Path.Direction.CCW);
        path4.addCircle(290 * density, measuredHeight / 2 - 31 * density, 30 * density, Path.Direction.CCW);
        path3.op(path4, Path.Op.DIFFERENCE);
        canvas.drawPath(path3, paint);
        paint.setColor(colorBlack);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(density);
        canvas.drawCircle(287 * density, measuredHeight / 2 - 22 * density, 40 * density, paint);
        canvas.drawCircle(290 * density, measuredHeight / 2 - 31 * density, 30 * density, paint);
    }


    private void drawcircle(Canvas canvas, Paint paint, String text) {
        paint.setColor(colorBlack);
        paint.setStrokeWidth(density);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(23.5f * density, measuredHeight / 2, 13.5f * density, paint);
        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(23.5f * density, measuredHeight / 2, 13f * density, paint);
        paint.setTextSize(18 * density);
        paint.setFakeBoldText(true);
        paint.setColor(colorBlack);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.rotate(90, measuredWidth / 2, measuredHeight / 2);
        canvas.drawText(text, measuredWidth / 2, wh / 2 + measuredWidth - density * 20 + 4f * density, paint);
    }


}
