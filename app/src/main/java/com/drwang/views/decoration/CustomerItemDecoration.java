package com.drwang.views.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.View;

/**
 * Created by Administrator on 2017/8/14.
 */

public class CustomerItemDecoration extends RecyclerView.ItemDecoration {
    Context context;
    float density;
    Paint paint;
    private final TextPaint textPaint;

    public CustomerItemDecoration(Context context) {
        super();
        this.context = context;
        density = context.getResources().getDisplayMetrics().density;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(20 * density);
        textPaint = new TextPaint(paint);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int left = parent.getLeft();
        int right = parent.getRight();
        int childCount = parent.getChildCount();
        Rect rect = new Rect();
        textPaint.getTextBounds("haha", 0, "haha".length(), rect);
        int line = (rect.top + rect.bottom) / 2;
        for (int i = 0; i < childCount; i++) {
            View childAt1 = parent.getChildAt(i);
            int childAdapterPosition = parent.getChildAdapterPosition(childAt1);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childAt1
                    .getLayoutParams();
            int height = childAt1.getHeight();
            int top = childAt1.getTop();
            int bottom = childAt1.getBottom();
            if (childAdapterPosition % 5 != 0) {
                paint.setColor(Color.BLACK);
                c.drawRect(left, 0, right, height, paint);
                paint.setColor(Color.RED);
                c.drawText("haha" + childAdapterPosition, left + 5,height + line, paint);
                continue;
            }
            paint.setColor(Color.BLACK);
            c.drawRect(left, top, right, bottom, paint);
            paint.setColor(Color.RED);
            c.drawText("haha" + childAdapterPosition, left + 5, bottom + line, paint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
        int childAdapterPosition = parent.getChildAdapterPosition(view);
        if (childAdapterPosition % 5 != 0) {
            outRect.set(0, 0, 0, 0);
        } else {
            outRect.set(0, (int) (0), 0, 0);
        }
    }
}
