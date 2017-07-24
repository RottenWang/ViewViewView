package com.drwang.views.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by hwj on 16/8/20.
 */
public class MedalBackgroundView extends View {

    private int width;
    private int height;
    private float hypotenuse;
    private float bottomWidth;
    private float x;
    private float y;
    private Path path;

    public MedalBackgroundView(Context context) {
        super(context);
        init();
    }

    public MedalBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MedalBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        width = getResources().getDisplayMetrics().widthPixels;
        height = getResources().getDisplayMetrics().heightPixels;
        hypotenuse = (float) Math.sqrt((width * width) + (height * height));
        hypotenuse = hypotenuse / 2;
        bottomWidth = (float) ((12.5 / 180) * Math.PI) * hypotenuse;
        bottomWidth = bottomWidth * 2;
        x = (width - bottomWidth) / 2;
        y = (height / 2) - hypotenuse;
    }

    int rotate;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rotate += 2;
        canvas.rotate(rotate, width / 2, height / 2);
        canvas.drawColor(Color.TRANSPARENT);
        Paint paint = new Paint();
        /* 去锯齿 */
        paint.setAntiAlias(true);
        /* 设置paint的颜色 */
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);
        /* 画一个实心三角形 */
        initPathAndDraw(canvas, paint);
        canvas.rotate(45f, width / 2, height / 2);
        initPathAndDraw(canvas, paint);
        canvas.rotate(45f, width / 2, height / 2);
        initPathAndDraw(canvas, paint);
        canvas.rotate(45f, width / 2, height / 2);
        initPathAndDraw(canvas, paint);
        canvas.rotate(45f, width / 2, height / 2);
        initPathAndDraw(canvas, paint);
        canvas.rotate(45f, width / 2, height / 2);
        initPathAndDraw(canvas, paint);
        canvas.rotate(45f, width / 2, height / 2);
        initPathAndDraw(canvas, paint);
        canvas.rotate(45f, width / 2, height / 2);
        initPathAndDraw(canvas, paint);
        postInvalidate();
    }

    private void initPathAndDraw(Canvas canvas, Paint paint) {
        path = new Path();
        path.moveTo(width / 2, height / 2);
        path.lineTo(x, y);
        path.lineTo(x + bottomWidth, y);
        path.close();
        canvas.drawPath(path, paint);
    }
}
