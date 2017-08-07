package com.drwang.views.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/8/7.
 */

@SuppressLint("AppCompatCustomView")
public class SpannableTextView extends TextView {
    private ClickableSpan mPressedSpan;
    private int pressedSpanDefaultColor;
    private int normalSpanDefaultColor;

    public SpannableTextView(Context context) {
        super(context);
        init();
    }

    public SpannableTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpannableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        pressedSpanDefaultColor = Color.GRAY;
        normalSpanDefaultColor = Color.TRANSPARENT;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getLayout() == null) {
            return super.onTouchEvent(event);
        }
        Spannable spannable = getLayout().getText() instanceof Spannable ? (Spannable) getLayout().getText() : null;
        try {
            if (spannable != null) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {

                    int x = (int) event.getX();
                    int y = (int) event.getY();

                    x -= getPaddingLeft();
                    y -= getPaddingTop();

                    x += getScrollX();
                    y += getScrollY();

                    Layout layout = getLayout();
                    int line = layout.getLineForVertical(y);
                    int off = layout.getOffsetForHorizontal(line, x);

                    ClickableSpan[] link = spannable.getSpans(off, off, ClickableSpan.class);

                    if (link.length != 0) {
                        mPressedSpan = link[0];
                        if (action == MotionEvent.ACTION_UP) {
                            mPressedSpan.onClick(this);
                            spannable.setSpan(new BackgroundColorSpan(normalSpanDefaultColor), spannable.getSpanStart(mPressedSpan), spannable.getSpanEnd(mPressedSpan), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            Selection.removeSelection(spannable);
                        } else if (action == MotionEvent.ACTION_DOWN) {
                            spannable.setSpan(new BackgroundColorSpan(pressedSpanDefaultColor), spannable.getSpanStart(mPressedSpan), spannable.getSpanEnd(mPressedSpan), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            Selection.removeSelection(spannable);
                        }
                        requestLayout();
                        invalidate();
                        return true;
                    } else {
                        spannable.setSpan(new BackgroundColorSpan(normalSpanDefaultColor), spannable.getSpanStart(mPressedSpan), spannable.getSpanEnd(mPressedSpan), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        requestLayout();
                        invalidate();
                        Selection.removeSelection(spannable);
                    }
                } else if (action == MotionEvent.ACTION_MOVE) {
                    ClickableSpan touchedSpan = getPressedSpan(spannable, event);
                    if (mPressedSpan != null && touchedSpan != mPressedSpan) {
                        spannable.setSpan(new BackgroundColorSpan(normalSpanDefaultColor), spannable.getSpanStart(mPressedSpan), spannable.getSpanEnd(mPressedSpan), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        requestLayout();
                        invalidate();
                        Selection.removeSelection(spannable);
                    }

                } else {
                    spannable.setSpan(new BackgroundColorSpan(normalSpanDefaultColor), spannable.getSpanStart(mPressedSpan), spannable.getSpanEnd(mPressedSpan), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    requestLayout();
                    invalidate();
                    Selection.removeSelection(spannable);
                }
            } else {
                spannable.setSpan(new BackgroundColorSpan(normalSpanDefaultColor), spannable.getSpanStart(mPressedSpan), spannable.getSpanEnd(mPressedSpan), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                requestLayout();
                invalidate();
                Selection.removeSelection(spannable);
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return super.onTouchEvent(event);
    }


    private ClickableSpan getPressedSpan(Spannable spannable, MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        x -= getPaddingLeft();
        y -= getPaddingTop();

        x += getScrollX();
        y += getScrollY();

        Layout layout = getLayout();
        int line = layout.getLineForVertical(y);
        int off = layout.getOffsetForHorizontal(line, x);

        ClickableSpan[] link = spannable.getSpans(off, off, ClickableSpan.class);
        ClickableSpan touchedSpan = null;
        if (link.length > 0) {
            touchedSpan = link[0];
        }
        return touchedSpan;
    }

    //设置spannable按住时的颜色
    public void setPressedSpanColor(int color) {
        pressedSpanDefaultColor = color;
    }

    //设置未按下时默认的颜色
    public void setNormalSpanColor(int color) {
        normalSpanDefaultColor = color;
    }

}
