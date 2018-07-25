package com.drwang.views.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;

public class TagLayout extends ViewGroup {
    public TagLayout(Context context) {
        super(context);
        init();
    }



    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {
        setBackgroundColor(Color.GRAY);
    }
    Rect[] rects;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        if (rects == null) {
            rects = new Rect[childCount];
        } else if (rects.length != childCount) {
            rects = Arrays.copyOf(this.rects, childCount);
        }
        View child;
        int paddingStart = getPaddingStart();
        int paddingEnd = getPaddingEnd();
        int getPaddingTop = getPaddingTop();
        int widthUsed = 0;
        int heightUsed = getPaddingTop;
        int lineHeight = 0;
        int lastRightMargin = 0;
        int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        Log.d("wangchen", "totalWidth = " + totalWidth);
        for (int i = 0; i < childCount; i++) {
            child = getChildAt(i);
            measureChildWithMargins(child, widthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed);
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            int leftMargin = layoutParams.leftMargin;
            int rightMargin = layoutParams.rightMargin;

            if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED
                    && child.getMeasuredWidth() + widthUsed + paddingStart + leftMargin + rightMargin + lastRightMargin + paddingEnd > totalWidth
                    || (child.getMeasuredState() & MEASURED_STATE_TOO_SMALL) != 0) {
                widthUsed = 0;
                lastRightMargin = 0;
                heightUsed += lineHeight;
                lineHeight = 0;
                measureChildWithMargins(child, widthMeasureSpec, widthUsed, heightMeasureSpec, heightUsed);
            }
            if (rects[i] == null) {
                rects[i] = new Rect();
            }
            rects[i].set(paddingStart + widthUsed + leftMargin + lastRightMargin, heightUsed, paddingStart + leftMargin + widthUsed + lastRightMargin + child.getMeasuredWidth(), heightUsed + child.getMeasuredHeight());
            widthUsed += child.getMeasuredWidth() + leftMargin + lastRightMargin;
            lineHeight = Math.max(lineHeight, child.getMeasuredHeight());
            lastRightMargin = rightMargin;

        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View child;
        Rect rect;
        for (int i = 0; i < getChildCount(); i++) {
            child = getChildAt(i);
            rect = rects[i];
            child.layout(rect.left, rect.top, rect.right, rect.bottom);
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
