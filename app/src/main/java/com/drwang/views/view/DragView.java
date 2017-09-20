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
 * Created by Administrator on 2017/8/29.
 */

public class DragView extends View {

    private Paint paint;
    private Path path;
    private float density;
    private CornerPathEffect cornerPathEffect;
    private int degree;

    public DragView(Context context) {
        this(context, null);
    }

    public DragView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DragView);
        degree = a.getInt(R.styleable.DragView_rotate_degree, 0);
        init();
    }

    private void init() {
        density = getResources().getDisplayMetrics().density;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        cornerPathEffect = new CornerPathEffect(density);
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.reset();
        paint.setStrokeWidth(density);
        float left = (float) (getWidth() / 2 - getWidth() / 2 * Math.cos(Math.toRadians(45)) + 0.5);
        float top = (float) (getWidth() / 2 - getWidth() / 2 * Math.cos(Math.toRadians(45)) + 0.5);
        path.moveTo(left, getHeight() / 2);
        path.lineTo(left, top);
        path.lineTo(getWidth() / 2, top);
        path.moveTo(left, top);
        path.lineTo(getWidth() - left, getHeight() - top);
        paint.setPathEffect(cornerPathEffect);
        canvas.save();
        canvas.rotate(degree, getWidth() / 2, getHeight() / 2);
        canvas.drawPath(path, paint);
        canvas.restore();
        paint.setPathEffect(null);
        paint.setStrokeWidth(0);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, paint);
    }
}
