package com.drwang.views.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by Administrator on 2017/8/28.
 */

public class CanvasView extends View {
    private Canvas mCanvas;
    private Paint paint;
    private Bitmap bitmapTemp;
    private Matrix matrix;
    float[] src = {0, 0, 0, 0, 0, 0, 0, 0};
    float[] dst = {0, 0, 0, 0, 0, 0, 0, 0};
    private boolean mSetBitmap;
    private Bitmap bitmap;
    private PorterDuffXfermode clearMode;
    private PorterDuffXfermode srcMode;

    public CanvasView(Context context) {
        this(context, null);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CanvasView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(100);
        paint.setColor(Color.RED);
        matrix = new Matrix();
        clearMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        srcMode = new PorterDuffXfermode(PorterDuff.Mode.SRC);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        matrix.reset();
        if (bitmapTemp == null) {
            return;
        }
        if (mSetBitmap) {
            initSrcAndDst();
        }
        paint.setXfermode(clearMode);
        mCanvas.drawPaint(paint);
        paint.setXfermode(srcMode);
        mCanvas.drawColor(Color.WHITE);
        matrix.setPolyToPoly(src, 0, dst, 0, 4);
        int save = mCanvas.save();
        mCanvas.concat(matrix);
        mCanvas.drawBitmap(bitmap, (getWidth() - bitmap.getWidth()) / 2, (getHeight() - bitmap.getHeight()) / 2, paint);
        mCanvas.restoreToCount(save);
        canvas.drawBitmap(bitmapTemp, 0, 0, paint);

    }

    private void initSrcAndDst() {

        float left = (mCanvas.getWidth() - bitmap.getWidth()) / 2;
        float right = mCanvas.getWidth() / 2 + bitmap.getWidth() / 2;
        float top = (mCanvas.getHeight() - bitmap.getHeight()) / 2;
        float bottom = mCanvas.getHeight() / 2 + bitmap.getHeight() / 2;
        //src point left top
        src[0] = left;
        src[1] = top;
        //src point right top
        src[2] = right;
        src[3] = top;
        //src point left bottom
        src[4] = left;
        src[5] = bottom;
        //src point right bottom
        src[6] = right;
        src[7] = bottom;
        //
        dst[0] = left;
        dst[1] = top;
        //src point right top
        dst[2] = right;
        dst[3] = top;
        //src point left bottom
        dst[4] = left;
        dst[5] = bottom;
        //src point right bottom
        dst[6] = right;
        dst[7] = bottom;
        mSetBitmap = false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmapTemp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(bitmapTemp);
    }

    public void setBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        this.bitmap = bitmap;
        mSetBitmap = true;
        invalidate();
    }

    float dx;
    float dy;
    float startX;
    float startY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                dx = event.getX() - startX;
                dy = event.getY() - startY;
                dst[0] = dst[0] + dx;
                dst[1] = dst[1] + dy;
                startX = startX + dx;
                startY = startY + dy;
                invalidate();
                Log.i("onTouchEvent", "onTouchEvent: dx = " + dx + ",dy = " + dy);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                startX = 0;
                startY = 0;
                break;
        }
        return true;
    }
}
