package com.drwang.views.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by wang on 2017/12/28.
 */

public class FloatingImageView extends ImageView {
    private static final float DEFAULT_POSITION = -1;
    private LayoutParamsType layoutParamsType = LayoutParamsType.RELATIVE_LAYOUT;
    private float density;

    public enum LayoutParamsType {
        RELATIVE_LAYOUT, //relativelayout
        COORDINATOR_LAYOUT,//android.support.design.widget  CoordinatorLayout
        FRAME_LAYOUT, //framelayout
        UNKNOWN_LAYOUT
    }


    public FloatingImageView(Context context) {
        this(context, null);
    }

    public FloatingImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        density = getResources().getDisplayMetrics().density;
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams instanceof RelativeLayout.LayoutParams) {
            layoutParamsType = LayoutParamsType.RELATIVE_LAYOUT;
        } else if (layoutParams instanceof CoordinatorLayout.LayoutParams) {
            layoutParamsType = LayoutParamsType.COORDINATOR_LAYOUT;
        } else if (layoutParams instanceof FrameLayout.LayoutParams) {
            layoutParamsType = LayoutParamsType.FRAME_LAYOUT;
        } else {
            layoutParamsType = LayoutParamsType.UNKNOWN_LAYOUT;
        }
    }

    private float startX = DEFAULT_POSITION;
    private float startY = DEFAULT_POSITION;
    private boolean isNeedExeClick = true;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getRawX();
                startY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startX == DEFAULT_POSITION || startY == DEFAULT_POSITION) {
                    startX = event.getRawX();
                    startY = event.getRawY();
                    return true;
                }
                float distanceX = event.getRawX();
                float distanceY = event.getRawY();
                float deltaX = 0;
                float deltaY = 0;
                deltaX += distanceX - startX;
                deltaY += distanceY - startY;
                startX = distanceX;
                startY = distanceY;
                if (Math.abs(deltaX) > 1 || Math.abs(deltaY) > 1) {
                    isNeedExeClick = false;
                    Log.d("wangchen", "deltaX = " + deltaX + ",deltaY = " + deltaY);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
                    layoutParams.leftMargin += (int) (deltaX + 0.5f);
                    layoutParams.topMargin += (int) (deltaY + 0.5f);
                    Log.d("wangchen", "layoutParams.leftMargin = " + layoutParams.leftMargin);
                    Log.d("wangchen", "layoutParams.topMargin = " + layoutParams.topMargin);
                    setLayoutParams(layoutParams);
                }


                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (isNeedExeClick) {
                    onclick();
                } else {

                }
                //重置所有的位置
                startX = DEFAULT_POSITION;
                startY = DEFAULT_POSITION;
                isNeedExeClick = true;
                break;
        }
        return true;

    }

    private void onclick() {
        Toast.makeText(getContext(), "哈哈哈哈哈", Toast.LENGTH_LONG).show();
    }
}
