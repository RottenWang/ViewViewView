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
import android.view.MotionEvent;
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

    float controlX = 0;
    float controlY = 0;
    boolean isFirst = true;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        path.reset();
        int width = getWidth();
        int height = getHeight();
        int centerX = width / 2;
        int centerY = height / 2;
        if (isFirst){
            controlY = -centerY / 2;
            controlX = centerX / 2 - 20;
            isFirst = false;
        }
        path.moveTo(20, centerY);
        path.rQuadTo(controlX, controlY, centerX - 20, 0);
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

    float startX = 0;
    float startY = 0;

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
                float moveX = event.getX();
                float moveY = event.getY();
                float distanceX = moveX - startX;
                float distanceY = moveY - startY;
                startX = moveX;
                startY = moveY;
                controlX += distanceX;
                controlY += distanceY;
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                startX = 0;
                startY = 0;
                break;
        }
        return true;

    }
}
