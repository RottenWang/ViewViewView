package com.drwang.views.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.PathInterpolator;

/**
 * Created by Administrator on 2017/7/24.
 */

public class ZBgView extends View {
    Paint paint;
    Path path;
    Path pathRec;
    float density;
    int imaginaryLineColor;//虚线的颜色;
    int measuredWidth;
    int measuredHeight;
    int recColor;
    RectF rectF;
    int colorRoundRect;
    String textRound;
    Rect rect;
    int colorCenterBg;
    String text2 = "优惠券";
    int colorGray = Color.parseColor("#BFBFC0");

    public ZBgView(Context context) {
        super(context);
        init();
    }

    public ZBgView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZBgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        rect = new Rect();
        textRound = "未领用";
        imaginaryLineColor = Color.parseColor("#E6E6E6");
        recColor = Color.parseColor("#683000");
        colorRoundRect = Color.parseColor("#6699FF");
        colorCenterBg = Color.parseColor("#E1D7CD");
        density = getResources().getDisplayMetrics().density;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(imaginaryLineColor);
        paint.setStrokeWidth(density);
        path = new Path();
        pathRec = new Path();
        paint.setPathEffect(new DashPathEffect(new float[]{density * 10, density * 5}, 0));
        rectF = new RectF();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measuredHeight = getMeasuredHeight();
        measuredWidth = getMeasuredWidth();
        rectF.left = (int) (5 * density + 0.5f);
        rectF.top = (int) (15 * density + 0.5f);
        rectF.right = (int) (53 * density + 0.5f);
        rectF.bottom = (int) (35 * density + 0.5f);
        paint.setTextSize(10 * density);
        paint.getTextBounds(textRound, 0, textRound.length(), rect);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.moveTo(15 * density, 5 * density);
        path.lineTo(measuredWidth - 15 * density, 5 * density);
        canvas.drawPath(path, paint);
        paint.setPathEffect(null);
        paint.setColor(recColor);
        paint.setStrokeWidth(density * 3);
        paint.setPathEffect(new CornerPathEffect(5 * density));
        paint.setStrokeJoin(Paint.Join.MITER);
        pathRec.moveTo(15 * density, 20 * density);
        pathRec.lineTo(measuredWidth * 0.85f, 20 * density);
        pathRec.lineTo(measuredWidth * 0.85f, 95 * density);
        pathRec.lineTo(measuredWidth * 0.80f, 110 * density);
        pathRec.lineTo(measuredWidth * 0.70f, 110 * density);//多画一点 为了避免视觉上的问题
        pathRec.moveTo(measuredWidth * 0.85f, 95 * density);
        pathRec.lineTo(measuredWidth * 0.80f, 95 * density);
        pathRec.lineTo(measuredWidth * 0.80f, 110 * density);
        pathRec.lineTo(15 * density, 110 * density);
        pathRec.lineTo(15 * density, 20 * density);
        canvas.drawPath(pathRec, paint);
        paint.setPathEffect(null);
        paint.setTextSize(15 * density);
        paint.setStrokeWidth(0);
        canvas.drawText("￥", 35 * density, 80 * density, paint);
        paint.setTextSize(26 * density);
        canvas.drawText("99", 54 * density, 80 * density, paint);
        paint.setTextSize(20 * density);
        canvas.drawText(".00", 84 * density, 78 * density, paint);
        paint.setStrokeWidth(3 * density);
        canvas.drawCircle(measuredWidth * 0.84f, 22 * density, 12 * density, paint);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(measuredWidth * 0.84f, 22 * density, 10.5f * density, paint);
        paint.setColor(recColor);
        paint.setTextSize(12 * density);
        paint.setStrokeWidth(1);
        canvas.drawText("租", measuredWidth * 0.84f - 5 * density, 27 * density, paint);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(colorRoundRect);
        canvas.drawRoundRect(rectF, 20 * density, 20 * density, paint);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(0);
        float xStart = rectF.left + ((rectF.right - rectF.left - (Math.abs(rect.right - rect.left))) / 2.0f);
        float yStart = rectF.bottom - ((rectF.bottom - rectF.top - (Math.abs(rect.bottom - rect.top))) / 2.0f);
        paint.setTextSize(10 * density);
        canvas.drawText(textRound, xStart, yStart, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(colorCenterBg);
        paint.setStrokeWidth(2 * density);
        canvas.drawLine(137 * density, 44 * density, 137 * density, 84 * density, paint);
        rectF.top = 44 * density;
        rectF.bottom = 64 * density;
        rectF.left = 150 * density;
        rectF.right = 200 * density;
        paint.setStrokeWidth(3 * density);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        canvas.drawRoundRect(rectF, 5 * density, 5 * density, paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setTextSize(10 * density);
        paint.setStrokeWidth(0);
        paint.setColor(Color.parseColor("#323232"));
        paint.getTextBounds(text2, 0, text2.length(), rect);
        float x = rectF.left + (((rectF.right - rectF.left) - (rect.right - rect.left)) / 2.0f);
        float y = rectF.bottom - (((rectF.bottom - rectF.top) - (rect.bottom - rect.top)) / 2.0f);
        canvas.drawText(text2, x, y, paint);
        paint.setColor(colorGray);
        paint.setTextSize(12 * density);
        canvas.drawText("满", 150 * density, 84 * density, paint);
        canvas.drawText("使用", 230 * density, 84 * density, paint);
        paint.setColor(Color.parseColor("#323232"));
        paint.setTextSize(10 * density);
        canvas.drawText("100元/2天", 170 * density, 82 * density, paint);
        paint.setColor(colorGray);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1 * density);
        canvas.drawCircle(340 * density,65 * density,10 * density,paint);
//        paint.setStrokeWidth();
        paint.setTextSize(14 * density);
        canvas.drawText("?",337 * density,70 * density,paint);

    }
}
