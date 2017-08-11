package com.drwang.views.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.drwang.views.R;

/**
 * Created by Administrator on 2017/8/9.
 */

public class CircleBgImageView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    float density;
    private Path path;
    private CornerPathEffect cornerPathEffect;
    private int width;
    private int height;

    enum DrawType {
        TYPE_SHARE, //分享type
        TYPE_EDIT,
        TYPE_DELETE,
        TYPE_MORE
    }

    private DrawType drawType = DrawType.TYPE_SHARE;

    public CircleBgImageView(Context context) {
        this(context, null);
    }

    public CircleBgImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleBgImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CircleBgImageView);
        int anInt = a.getInt(R.styleable.CircleBgImageView_draw_type, 0);
        drawType = DrawType.values()[anInt];
        init();
    }

    private void init() {
        setBackgroundColor(Color.TRANSPARENT);
        density = getResources().getDisplayMetrics().density;
        paint.setColor(Color.WHITE);
        path = new Path();
        paint.setStyle(Paint.Style.STROKE);
        paint.setFilterBitmap(true);
        cornerPathEffect = new CornerPathEffect(density);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(density / 2);
        canvas.drawCircle(width / 2, height / 2, width / 2 - (density * 2), paint);
        switch (drawType) {
            case TYPE_SHARE:
                drawShareShape(canvas);
                break;
            case TYPE_EDIT:
                drawEditShape(canvas);
                break;
            case TYPE_DELETE:
                drawDeleteShape(canvas);
                break;
            case TYPE_MORE:
                drawMoreShape(canvas);
                break;
        }

    }

    private void drawEditShape(Canvas canvas) {
        path.reset();
        path.moveTo((width / 3) * 2 - 4 * density, height / 3);
        path.lineTo(width / 3, height / 3);
        path.lineTo(width / 3, height / 3 * 2);
        path.lineTo(width / 3 * 2, height / 3 * 2);
        path.lineTo(width / 3 * 2, height / 3 + 4 * density);
        path.moveTo(width / 3 * 2, height / 3);
        path.lineTo(width / 2, height / 2);
        paint.setStrokeWidth(density);
        canvas.drawPath(path, paint);
    }

    private void drawDeleteShape(Canvas canvas) {
        path.reset();
        path.moveTo(width / 3 - 2 * density, height / 3);
        path.lineTo(width / 3 * 2 + 2 * density, height / 3);
        path.moveTo(width / 3, height / 3);
        path.lineTo(width / 3, height / 3 * 2);
        path.lineTo(width / 3 * 2, height / 3 * 2);
        path.lineTo(width / 3 * 2, height / 3);
        path.moveTo(width / 2 - 2 * density, height / 3);
        path.lineTo(width / 2 - 2 * density, height / 3 - 2 * density);
        path.lineTo(width / 2 + 2 * density, height / 3 - 2 * density);
        path.lineTo(width / 2 + 2 * density, height / 3 - 2);
        paint.setStrokeWidth(density);
        canvas.drawPath(path, paint);

    }

    private void drawMoreShape(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(density * 2);
        paint.setPathEffect(cornerPathEffect);
        canvas.drawPoint(width / 2 - 5 * density, height / 2, paint);
        canvas.drawPoint(width / 2, height / 2, paint);
        canvas.drawPoint(width / 2 + 5 * density, height / 2, paint);
        paint.setPathEffect(null);
    }

    private void drawShareShape(Canvas canvas) {
        path.reset();
        path.moveTo(width / 3 + 3 * density, height / 3);
        path.lineTo(width / 3, height / 3);
        path.lineTo(width / 3, (height / 3) * 2);
        path.lineTo((width / 3) * 2, (height / 3) * 2);
        path.lineTo((width / 3) * 2, (height / 3) * 2 - 3 * density);
        path.moveTo(width / 3 + 5 * density, height / 3);
        path.lineTo((width / 3) * 2, height / 3);
        path.lineTo((width / 3) * 2, (height / 3) * 2 - 5 * density);
        path.moveTo((width / 3) * 2, height / 3);
        path.lineTo(width / 2, height / 2);
        paint.setPathEffect(cornerPathEffect);
        paint.setStrokeWidth(density);
        canvas.drawPath(path, paint);
        paint.setPathEffect(null);
    }

}
