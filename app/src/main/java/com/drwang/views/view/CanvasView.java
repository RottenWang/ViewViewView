package com.drwang.views.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.MediaScannerConnection;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.drwang.views.util.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


/**
 * Created by Administrator on 2017/8/28.
 */

public class CanvasView extends View {
    private Canvas mCanvas;
    private boolean hasInvokeInterfaceImpl;//是否已调用过接口impl
    private Paint paint;
    private Bitmap bitmapTemp;
    private Matrix matrix;
    float[] src = {0, 0, 0, 0, 0, 0, 0, 0};
    float[] dst = {0, 0, 0, 0, 0, 0, 0, 0};
    private boolean mSetBitmap;
    private Bitmap bitmap;
    private PorterDuffXfermode clearMode;
    private PorterDuffXfermode srcMode;
    private String name;
    private Matrix matrixSave;
    private OnSizeChangedInterface mOnSizeChangedInterface;
    private float left;
    private float right;
    private float top;
    private float bottom;

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
        left = (mCanvas.getWidth() - bitmap.getWidth()) / 2;
        right = mCanvas.getWidth() / 2 + bitmap.getWidth() / 2;
        top = (mCanvas.getHeight() - bitmap.getHeight()) / 2;
        bottom = mCanvas.getHeight() / 2 + bitmap.getHeight() / 2;


        if (!hasInvokeInterfaceImpl && mOnSizeChangedInterface != null) {
            hasInvokeInterfaceImpl = true;
            mOnSizeChangedInterface.onSizeHasChanged(left, top, right, bottom);
        }
    }

    public void setBitmap(Bitmap bitmap, String name) {
        if (bitmap == null || name == null) {
            return;
        }
        this.name = name;
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

    public void saveImages() {
        initSaveMatrix();
        Bitmap bitmap2 = Bitmap.createBitmap(this.bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrixSave, false);
        saveImage(FileUtil.getFoldPath(), System.currentTimeMillis() + name, bitmap2);
    }

    private void initSaveMatrix() {
        matrixSave = new Matrix();
        float[] srcSave = {0, 0,//left top
                bitmap.getWidth(),//right top
                0, 0, bitmap.getHeight(),//left bottom
                bitmap.getWidth(), bitmap.getHeight()};//right bottom;
        float[] dstSave = {0, 0,//left top
                bitmap.getWidth(),//right top
                0, 0, bitmap.getHeight(),//left bottom
                bitmap.getWidth(), bitmap.getHeight()};//right bottom;
        dstSave[0] = dstSave[0] + dst[0] - src[0];
        dstSave[1] = dstSave[1] + dst[1] - src[1];
        dstSave[2] = dstSave[2] + dst[2] - src[2];
        dstSave[3] = dstSave[3] + dst[3] - src[3];
        dstSave[4] = dstSave[4] + dst[4] - src[4];
        dstSave[5] = dstSave[5] + dst[5] - src[5];
        dstSave[6] = dstSave[6] + dst[6] - src[6];
        dstSave[7] = dstSave[7] + dst[7] - src[7];
        matrixSave.setPolyToPoly(srcSave, 0, dstSave, 0, 4);
    }

    private void saveImage(final String folderName, final String fileName, final Bitmap image) {
        File file = new File(folderName, fileName);
        try {
            file.getParentFile().mkdirs();
            image.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(file));
            MediaScannerConnection.scanFile(getContext(),
                    new String[]{
                            file.toString()
                    }, null,
                    (path, uri) -> {
                        Log.i("saveImage", "saveImage: path = " + path + "uri = " + uri);
                    });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setOnSizeChangedInterface(OnSizeChangedInterface onSizeChangedInterface) {
        this.mOnSizeChangedInterface = onSizeChangedInterface;
    }

    public interface OnSizeChangedInterface {
        void onSizeHasChanged(float left, float top, float right, float bottom);
    }
}
