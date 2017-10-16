package com.drwang.views.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.drwang.views.R;

import java.io.InputStream;

import static com.drwang.views.view.ShadowView.Type.TYPE_INPUTSTREM;
import static com.drwang.views.view.ShadowView.Type.TYPE_ORIGIN;
import static com.drwang.views.view.ShadowView.Type.TYPE_RES;

/**
 * Created by Administrator on 2017/7/31.
 */

public class ShadowView extends View {
    Type type;

    enum Type {
        TYPE_ORIGIN,
        TYPE_INPUTSTREM,
        TYPE_RES

    }

    InputStream mInputStream;
    private Paint paint;
    int measuredWidth;
    int measuredHeight;
    Bitmap mBitmap;
    BitmapFactory.Options mOptions;
    int mRes;
    Matrix matrix;

    public ShadowView(Context context) {
        this(context, null);
    }

    public ShadowView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ShadowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        paint = new Paint();
        mOptions = new BitmapFactory.Options();
        mOptions.inJustDecodeBounds = true;
        type = TYPE_ORIGIN;
        matrix = new Matrix();
        BitmapFactory.decodeResource(getResources(), R.drawable.default_bg, mOptions);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initBitmapOptions();
        drawBitmap(canvas);
    }

    private void drawBitmap(Canvas canvas) {
        canvas.save();
        if (mBitmap != null) {
            matrix.setTranslate((measuredWidth - mBitmap.getWidth()) / 2.0f, (measuredHeight - mBitmap.getHeight()) / 2.0f);
            canvas.setMatrix(matrix);
            canvas.drawBitmap(mBitmap, 0, 0, paint);
        }
        canvas.restore();
    }

    private void initBitmapOptions() {
        int width = mOptions.outWidth;
        int height = mOptions.outHeight;
        mOptions.inSampleSize = (int) (width / (measuredWidth * 1.0f) > height / (measuredHeight * 1.0f) ?
                (width / measuredWidth * 1.0f) + 0.5f : height / (measuredHeight * 1.0f) + 0.5f);
        mOptions.inJustDecodeBounds = false;
        reloadingBitmap();
    }

    private void reloadingBitmap() {
        switch (type) {
            case TYPE_ORIGIN:
                mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_bg, mOptions);
                break;
            case TYPE_INPUTSTREM:
                mBitmap = BitmapFactory.decodeStream(mInputStream, new Rect(), mOptions);
                mInputStream = null;
                break;
            case TYPE_RES:
                mBitmap = BitmapFactory.decodeResource(getResources(), mRes, mOptions);
                break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        measuredWidth = w;
        measuredHeight = h;
    }

    public void setBitmap(InputStream inputStream) {
        mOptions.inJustDecodeBounds = true;
        type = TYPE_INPUTSTREM;
        mInputStream = inputStream;
        mBitmap = BitmapFactory.decodeStream(inputStream, new Rect(), mOptions);
        invalidate();
    }

    public void setBitmapRes(@DrawableRes int Res) {
        mOptions.inJustDecodeBounds = true;
        type = TYPE_RES;
        mRes = Res;
        mBitmap = BitmapFactory.decodeResource(getResources(), Res, mOptions);
        invalidate();
    }

}
