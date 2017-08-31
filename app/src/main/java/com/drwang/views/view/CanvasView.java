package com.drwang.views.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.MediaScannerConnection;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.drwang.views.App;
import com.drwang.views.util.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

import static com.drwang.views.view.CanvasView.DrawType.TYPE_DRAG;
import static com.drwang.views.view.CanvasView.DrawType.TYPE_POLY_TO_POLY;
import static com.drwang.views.view.CanvasView.TouchArea.LEFT_BOTTOM;
import static com.drwang.views.view.CanvasView.TouchArea.LEFT_TOP;
import static com.drwang.views.view.CanvasView.TouchArea.OTHER;
import static com.drwang.views.view.CanvasView.TouchArea.RIGHT_BOTTOM;
import static com.drwang.views.view.CanvasView.TouchArea.RIGHT_TOP;


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
    private String name;
    private Matrix matrixSave;
    private OnSizeChangedInterface mOnSizeChangedInterface;
    private float left;
    private float right;
    private float top;
    private float bottom;
    private Path path;
    private float density;
    private CornerPathEffect cornerPathEffect;
    private boolean init;
    private float radius;
    private float delta;

    private float translateX;
    private float translateY;
    private String imagePath;
    private GPUImageFilter filter;
    private float scale;
    private boolean isSaveOriginalImage; //是否保存原图

    enum DrawType {
        TYPE_DRAG, TYPE_POLY_TO_POLY,
    }

    enum TouchArea {
        LEFT_TOP, RIGHT_TOP, LEFT_BOTTOM, RIGHT_BOTTOM, OTHER
    }

    DrawType dType;
    TouchArea tArea;

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
        paint.setDither(true);
        paint.setFilterBitmap(true);
        matrix = new Matrix();
        clearMode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        path = new Path();
        density = getResources().getDisplayMetrics().density;
        radius = 10 * density;
        delta = 7 * density;
        cornerPathEffect = new CornerPathEffect(density);
        init = true;
        dType = TYPE_POLY_TO_POLY;
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
        paint.setXfermode(null);
        mCanvas.drawColor(Color.WHITE);
        matrix.setPolyToPoly(src, 0, dst, 0, 4);
        matrix.postTranslate(translateX, translateY);
        int save = mCanvas.save();
        mCanvas.concat(matrix);
        if (bitmap != null) {
            mCanvas.drawBitmap(bitmap, (getWidth() - bitmap.getWidth()) / 2, (getHeight() - bitmap.getHeight()) / 2, paint);
        }
        mCanvas.restoreToCount(save);
        if (dType == TYPE_POLY_TO_POLY) {
            drawCircle(mCanvas);
        }
        canvas.drawBitmap(bitmapTemp, 0, 0, paint);

    }

    float leftCenterTop;
    float topCenterLeft;
    float rightCenterTop;
    float bottomCenterLeft;
    float leftCenterBottom;
    float topCenterRight;
    float rightCenterBottom;
    float bottomCenterRight;

    private void drawCircle(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        if (init) {
            if (left < radius) {
                leftCenterTop = radius;
            } else {
                leftCenterTop = left;
            }
            if (top < radius) {
                topCenterLeft = radius;
            } else {
                topCenterLeft = top;
            }
            if (getWidth() - right < radius) {
                rightCenterTop = getWidth() - radius;
            } else {
                rightCenterTop = right;
            }
            if (getHeight() - bottom < radius) {
                bottomCenterLeft = getHeight() - radius;
            } else {
                bottomCenterLeft = bottom;
            }
            if (right - left < 2 * radius) {
                leftCenterTop = (right + left) / 2 - radius;
                rightCenterTop = (right + left) / 2 + radius;
            }
            if (bottom - top < 2 * radius) {
                topCenterLeft = (top + bottom) / 2 - radius;
                bottomCenterLeft = (top + bottom) / 2 + radius;
            }
            leftCenterBottom = leftCenterTop;
            rightCenterBottom = rightCenterTop;
            topCenterRight = topCenterLeft;
            bottomCenterRight = bottomCenterLeft;
        }

        //draw left top arrow
        path.reset();
        float sin45Length = (float) (radius * (Math.sin(Math.toRadians(45))));
        path.moveTo(leftCenterTop - sin45Length, topCenterLeft - sin45Length + delta);
        path.lineTo(leftCenterTop - sin45Length, topCenterLeft - sin45Length);
        path.lineTo(leftCenterTop - sin45Length + delta, topCenterLeft - sin45Length);
        path.moveTo(leftCenterTop - sin45Length, topCenterLeft - sin45Length);
        path.lineTo(leftCenterTop + sin45Length, topCenterLeft + sin45Length);
        paint.setStrokeWidth(density);
        paint.setPathEffect(cornerPathEffect);
        canvas.drawPath(path, paint);

        //draw right top arrow
        path.reset();
        path.moveTo(rightCenterTop + sin45Length - delta, topCenterRight - sin45Length);
        path.lineTo(rightCenterTop + sin45Length, topCenterRight - sin45Length);
        path.lineTo(rightCenterTop + sin45Length, topCenterRight - sin45Length + delta);
        path.moveTo(rightCenterTop + sin45Length, topCenterRight - sin45Length);
        path.lineTo(rightCenterTop - sin45Length, topCenterRight + sin45Length);
        canvas.drawPath(path, paint);

        //draw left bottom arrow
        path.reset();
        path.moveTo(leftCenterBottom - sin45Length, bottomCenterLeft + sin45Length - delta);
        path.lineTo(leftCenterBottom - sin45Length, bottomCenterLeft + sin45Length);
        path.lineTo(leftCenterBottom - sin45Length + delta, bottomCenterLeft + sin45Length);
        path.moveTo(leftCenterBottom - sin45Length, bottomCenterLeft + sin45Length);
        path.lineTo(leftCenterBottom + sin45Length, bottomCenterLeft - sin45Length);
        canvas.drawPath(path, paint);
        //draw right bottom arrow
        path.reset();
        path.moveTo(rightCenterBottom + sin45Length, bottomCenterRight + sin45Length - delta);
        path.lineTo(rightCenterBottom + sin45Length, bottomCenterRight + sin45Length);
        path.lineTo(rightCenterBottom + sin45Length - delta, bottomCenterRight + sin45Length);
        path.moveTo(rightCenterBottom + sin45Length, bottomCenterRight + sin45Length);
        path.lineTo(rightCenterBottom - sin45Length, bottomCenterRight - sin45Length);
        canvas.drawPath(path, paint);
        paint.setPathEffect(null);
        paint.setStrokeWidth(0);
        canvas.drawCircle(leftCenterTop, topCenterLeft, radius, paint);
        canvas.drawCircle(rightCenterTop, topCenterRight, radius, paint);
        canvas.drawCircle(leftCenterBottom, bottomCenterLeft, radius, paint);
        canvas.drawCircle(rightCenterBottom, bottomCenterRight, radius, paint);
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
        //dst point left top
        dst[0] = left;
        dst[1] = top;
        //dst point right top
        dst[2] = right;
        dst[3] = top;
        //dst point left bottom
        dst[4] = left;
        dst[5] = bottom;
        //dst point right bottom
        dst[6] = right;
        dst[7] = bottom;
        mSetBitmap = false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bitmapTemp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(bitmapTemp);
        if (bitmap == null) {
            return;
        }
        initPoint();

        if (!hasInvokeInterfaceImpl && mOnSizeChangedInterface != null) {
            hasInvokeInterfaceImpl = true;
            mOnSizeChangedInterface.onSizeHasChanged(left, top, right, bottom);
        }
    }

    private void initPoint() {
        left = (mCanvas.getWidth() - bitmap.getWidth()) / 2;
        right = mCanvas.getWidth() / 2 + bitmap.getWidth() / 2;
        top = (mCanvas.getHeight() - bitmap.getHeight()) / 2;
        bottom = mCanvas.getHeight() / 2 + bitmap.getHeight() / 2;
    }

    public void setBitmap(Bitmap bitmap, String name, String path, GPUImageFilter filter, float scale) {
        if (bitmap == null || name == null || path == null) {
            return;
        }
        this.name = name;
        this.bitmap = bitmap;
        this.imagePath = path;
        this.scale = scale;
        this.filter = filter;
        mSetBitmap = true;
        invalidate();
    }

    float dx;
    float dy;
    float startX;
    float startY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        init = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                calculatorTouchArea(startX, startY);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("onTouchEvent", "onTouchEvent: dx = " + dx + ",dy = " + dy);
                calculatorMove(event);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                dType = TYPE_POLY_TO_POLY;
                tArea = null;
                startX = 0;
                startY = 0;
                dx = 0;
                dy = 0;
                invalidate();
                break;
        }
        return true;
    }

    private void calculatorMove(MotionEvent event) {
        dx += event.getX() - startX;
        dy += event.getY() - startY;
        startX = startX + dx;
        startY = startY + dy;
        if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {
            switch (tArea) {
                case LEFT_TOP:
                    dType = TYPE_POLY_TO_POLY;
                    dst[0] = dst[0] + dx;
                    dst[1] = dst[1] + dy;
                    leftCenterTop += dx;
                    topCenterLeft += dy;
                    break;
                case RIGHT_TOP:
                    dType = TYPE_POLY_TO_POLY;
                    dst[2] = dst[2] + dx;
                    dst[3] = dst[3] + dy;
                    rightCenterTop += dx;
                    topCenterRight += dy;
                    break;
                case LEFT_BOTTOM:
                    dType = TYPE_POLY_TO_POLY;
                    dst[4] = dst[4] + dx;
                    dst[5] = dst[5] + dy;
                    leftCenterBottom += dx;
                    bottomCenterLeft += dy;
                    break;
                case RIGHT_BOTTOM:
                    dType = TYPE_POLY_TO_POLY;
                    dst[6] = dst[6] + dx;
                    dst[7] = dst[7] + dy;
                    rightCenterBottom += dx;
                    bottomCenterRight += dy;
                    break;
                case OTHER:
                    dType = TYPE_DRAG;
                    translateX += dx;
                    translateY += dy;
                    leftCenterTop += dx;
                    topCenterLeft += dy;
                    rightCenterTop += dx;
                    topCenterRight += dy;
                    leftCenterBottom += dx;
                    bottomCenterLeft += dy;
                    rightCenterBottom += dx;
                    bottomCenterRight += dy;
                    break;
            }
            dx = 0;
            dy = 0;
            invalidate();
        }


    }

    private void calculatorTouchArea(float startX, float startY) {
        float radius = this.radius * 1.3f;
        if (startY >= topCenterLeft - radius * 1.5f && startY <= topCenterLeft + radius && startX >= leftCenterTop - radius * 1.5f && startX <= leftCenterTop + radius) {
            tArea = LEFT_TOP;
        } else if (startY >= topCenterRight - radius * 1.5f && startY <= topCenterRight + radius && startX >= rightCenterTop - radius && startX <= rightCenterTop + radius * 1.5f) {
            tArea = RIGHT_TOP;
        } else if (startY >= bottomCenterLeft - radius && startY <= bottomCenterLeft + radius * 1.5f && startX >= leftCenterBottom - radius * 1.5f && startX <= leftCenterBottom + radius) {
            tArea = LEFT_BOTTOM;
        } else if (startY >= bottomCenterRight - radius && startY <= bottomCenterRight + radius * 1.5f && startX >= rightCenterBottom - radius && startX <= rightCenterBottom + radius * 1.5f) {
            tArea = RIGHT_BOTTOM;
        } else {
            tArea = OTHER;
        }
    }

    public Bitmap getCurrentBitmap() {
        return saveImages(false);
    }

    public Bitmap saveImages(boolean isSaveLocal) {
        Bitmap bitmap2;
        if (isSaveOriginalImage) {
            GPUImage gpuImage = new GPUImage(getContext());
            gpuImage.setFilter(filter);
            Bitmap bitmapOriginal = BitmapFactory.decodeFile(imagePath);
            Bitmap bitmapWithFilterApplied = gpuImage.getBitmapWithFilterApplied(bitmapOriginal);
            initSaveMatrix(bitmapWithFilterApplied, scale);
            bitmap2 = Bitmap.createBitmap(bitmapWithFilterApplied, 0, 0, bitmapWithFilterApplied.getWidth(), bitmapWithFilterApplied.getHeight(), matrixSave, false);
        } else {
            initSaveMatrix(bitmap, 1);
            bitmap2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrixSave, false);
        }
        if (isSaveLocal) {
            saveImage(FileUtil.getFoldPath(), System.currentTimeMillis() + name, bitmap2);
        }
        return bitmap2;

//        Bitmap bitmap2 = Bitmap.createBitmap(this.bitmap, 0, 0, this.bitmap.getWidth(), this.bitmap.getHeight(), matrixSave, false);
    }

    private void initSaveMatrix(Bitmap bitmap, float scale) {
        matrixSave = new Matrix();
        float[] srcSave = {0, 0,//left top
                bitmap.getWidth(),//right top
                0, 0, bitmap.getHeight(),//left bottom
                bitmap.getWidth(), bitmap.getHeight()};//right bottom;
        float[] dstSave = {0, 0,//left top
                bitmap.getWidth(),//right top
                0, 0, bitmap.getHeight(),//left bottom
                bitmap.getWidth(), bitmap.getHeight()};//right bottom;
        dstSave[0] = dstSave[0] + (dst[0] - src[0]) / scale;
        dstSave[1] = dstSave[1] + (dst[1] - src[1]) / scale;
        dstSave[2] = dstSave[2] + (dst[2] - src[2]) / scale;
        dstSave[3] = dstSave[3] + (dst[3] - src[3]) / scale;
        dstSave[4] = dstSave[4] + (dst[4] - src[4]) / scale;
        dstSave[5] = dstSave[5] + (dst[5] - src[5]) / scale;
        dstSave[6] = dstSave[6] + (dst[6] - src[6]) / scale;
        dstSave[7] = dstSave[7] + (dst[7] - src[7]) / scale;
        matrixSave.setPolyToPoly(srcSave, 0, dstSave, 0, 4);
    }

    public void setSaveOriginalImage(boolean saveOriginalImage) {
        isSaveOriginalImage = saveOriginalImage;
    }

    private void saveImage(final String folderName, final String fileName, final Bitmap image) {
        File file = new File(folderName, fileName);
        try {
            file.getParentFile().mkdirs();
            image.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(file));
            MediaScannerConnection.scanFile(App.sApplication,
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
