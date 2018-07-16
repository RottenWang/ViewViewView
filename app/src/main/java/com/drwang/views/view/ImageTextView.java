package com.drwang.views.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.drwang.views.R;

public class ImageTextView extends View {
    private String str = "This class represents the basic building block for user interface components. A View" +
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
        textPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,Resources.getSystem().getDisplayMetrics()));
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.default_bg);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        if (staticLayout == null) {
            staticLayout = new StaticLayout(str, textPaint, measuredWidth, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        staticLayout.draw(canvas);
        canvas.drawBitmap(bitmap, getWidth() / 2 - bitmap.getWidth() / 2, getHeight() / 2 - bitmap.getHeight() / 2, paint);
    }
}
