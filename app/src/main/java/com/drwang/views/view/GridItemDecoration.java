package com.drwang.views.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by wang on 2018/2/7.
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration {
    int width;
    int color;
    Paint paint;
    int[] colors = {Color.RED, Color.parseColor("#FFFF7119"), Color.YELLOW, Color.GREEN, Color.BLUE, Color.parseColor("#065279"), Color.parseColor("#AAC20AFF")};
    LinearGradient mLinearGradient;

    public GridItemDecoration(int width, int color) {
        this.width = width;
        this.color = color;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) (layoutManager);
            int spanCount = gridLayoutManager.getSpanCount();
            int childCount = parent.getChildCount();
            for (int position = 0; position < childCount; position++) {

                boolean isFirst = position / spanCount == 0;
                View childAt1 = parent.getChildAt(position);
                int childAdapterPosition = parent.getChildAdapterPosition(childAt1);
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) childAt1
                        .getLayoutParams();
                int height = childAt1.getHeight();
                boolean isFirstLine = position < spanCount;

                int top = childAt1.getTop();
                int right = childAt1.getRight();
                int bottom = childAt1.getBottom();
                int left = childAt1.getLeft();

                if (position % spanCount == 0) {
//                    mLinearGradient = new LinearGradient(0, top, left, bottom, colors, null, Shader.TileMode.CLAMP);
//                    paint.setShader(mLinearGradient);
                    c.drawRect(0, top, left, bottom, paint);
                    c.drawRect(0, bottom, right + width, bottom + width, paint);
                    if (isFirstLine) {
                        c.drawRect(0, 0, right + width, top, paint);
                    }
                } else {
                    if (isFirstLine) {
                        c.drawRect(left, 0, right + width, top, paint);
                    }
                }

                c.drawRect(left, bottom, right + width, bottom + width, paint);
                c.drawRect(right, top, right + width, bottom, paint);
            }

        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) (layoutManager);
            int spanCount = gridLayoutManager.getSpanCount();
            int position = gridLayoutManager.getPosition(view);
            boolean isFirst = position / spanCount == 0;
            if (position % spanCount == 0) {
                if (isFirst) {
                    outRect.set(width, width, width / 2, width);
                } else {
                    outRect.set(width, 0, width / 2, width);
                }
            } else if (position % spanCount == spanCount - 1) {
                if (isFirst) {
                    outRect.set(width / 2, width, width, width);
                } else {
                    outRect.set(width / 2, 0, width, width);
                }
            } else {
                if (isFirst) {
                    outRect.set(width / 2, width, width / 2, width);
                } else {
                    outRect.set(width / 2, 0, width / 2, width);
                }
            }

        } else {
            super.getItemOffsets(outRect, view, parent, state);
        }
    }
}
