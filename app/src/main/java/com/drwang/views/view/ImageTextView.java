package com.drwang.views.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.drwang.views.R;

public class ImageTextView extends View {
    private String text = "This class represents the basic building block for user interface components. A View" +
            "occupies a rectangular area on the screen and is responsible for drawing and" +
            "event handling. View is the base class for <em>widgets</em>, which are" +
            "used to create interactive UI components (buttons, text fields, etc.). The" +
            "{@link android.view.ViewGroup} subclass is the base class for <em>layouts</em>, which" +
            "are invisible containers that hold other Views (or other ViewGroups) and define" +
            "their layout properties.";
    private TextPaint textPaint;
    private Paint paint;
    StaticLayout staticLayout;
    private Bitmap bitmap;
    int bitmapWidth;
    int bitmapHeight;
    int width;
    int height;
    private Paint.FontMetrics fontMetrics;
    float[] measuredWidth = new float[1];

    public ImageTextView(Context context) {
        this(context, null);
    }

    public ImageTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, Resources.getSystem().getDisplayMetrics()));
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_bg);
        bitmapWidth = bitmap.getWidth();
        bitmapHeight = bitmap.getHeight();
        fontMetrics = textPaint.getFontMetrics();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int measuredWidth = getMeasuredWidth();
//        if (staticLayout == null) {
//            staticLayout = new StaticLayout(str, textPaint, measuredWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
//        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int hStart = (height - bitmapHeight) / 2;
        int hEnd = height / 2 + bitmapHeight / 2;
        int wStart = width / 2 - bitmapWidth / 2;
        int wEnd = width / 2 + bitmapWidth / 2;
        float y = -fontMetrics.top;
        int totalWidth;
        if (y + fontMetrics.top > hStart && y + fontMetrics.top < hEnd || y + fontMetrics.bottom > hStart && y + fontMetrics.bottom <hEnd) {
            totalWidth = width - bitmapWidth;
        } else {
            totalWidth = width;
        }
        int start = 0;
        int count = textPaint.breakText(text, 0, text.length(), true, totalWidth, measuredWidth);
        while (count > 0) {
            if (totalWidth == width) {
                canvas.drawText(text, start, start + count, 0, y, textPaint);
                start += count;
            } else {
                count = textPaint.breakText(text, start, text.length(), true, totalWidth / 2, measuredWidth);
                canvas.drawText(text, start, start + count, 0, y, textPaint);
                start += count;
                count = textPaint.breakText(text, start, text.length(), true, totalWidth / 2, measuredWidth);
                canvas.drawText(text, start, start + count, totalWidth / 2 + bitmapWidth, y, textPaint);
                start += count;
            }
            y += textPaint.getFontSpacing();
            if (y + fontMetrics.top > hStart && y + fontMetrics.top < hEnd
                    || y + fontMetrics.bottom > hStart && y + fontMetrics.bottom <hEnd
                    ) {
                totalWidth = width - bitmapWidth;
            } else {
                totalWidth = width;
            }
            count = textPaint.breakText(text, start, text.length(), true, totalWidth, measuredWidth);
            Log.d("wangchen", "count = " + count + "y = " + y);
        }
//        staticLayout.draw(canvas);
        canvas.drawBitmap(bitmap, getWidth() / 2 - bitmap.getWidth() / 2, getHeight() / 2 - bitmap.getHeight() / 2, paint);
    }
}
