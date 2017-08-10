package com.drwang.views.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by Administrator on 2017/8/9.
 */

public class CircleBgImageView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    float density;
    private Path path;
    private CornerPathEffect cornerPathEffect;

    public CircleBgImageView(Context context) {
        this(context, null);
    }

    public CircleBgImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleBgImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
        int width = getWidth();
        int height = getHeight();
        paint.setStrokeWidth(density / 2);
        canvas.drawCircle(width / 2, height / 2, width / 2 - (density * 2), paint);
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
