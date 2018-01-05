package com.drwang.views.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wang on 2018/1/5.
 */

public class ArtTextView extends View {
    TextPaint textPaint;
    Paint paint;
    LinearGradient mLinearGradient;
    String text;
    Path path;
    private float density;
    int[] colors = {Color.RED, Color.parseColor("#FFFF7119"), Color.YELLOW, Color.GREEN, Color.BLUE, Color.parseColor("#065279"), Color.parseColor("#AAC20AFF")};
    float[] positions = {0.2f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.3f};

    public ArtTextView(Context context) {
        this(context, null);
    }

    public ArtTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArtTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        density = getResources().getDisplayMetrics().density;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(density * 15);
        text = "2018 Happy New Year~~~";
        path = new Path();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.reset();
        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;
        path.moveTo(20, centerY);
        path.rQuadTo(centerX / 2 - 20, -centerY / 2, centerX - 20, 0);
        if (mLinearGradient == null) {
            mLinearGradient = new LinearGradient(0, 0, centerX, 0, colors, null, Shader.TileMode.CLAMP);
        }
        textPaint.setShader(mLinearGradient);
        canvas.save();
        canvas.translate(100, 100);
        canvas.drawTextOnPath(text, path, 0, 0, textPaint);
        textPaint.setShader(null);
        canvas.restore();
    }
}
