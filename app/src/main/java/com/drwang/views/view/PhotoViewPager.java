package com.drwang.views.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/8/9.
 */

public class PhotoViewPager extends ViewPager {
    public PhotoViewPager(Context context) {
        super(context);
    }

    public PhotoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            Log.e("onInterceptTouchEvent", "onInterceptTouchEvent: " + e.getMessage());
            Toast.makeText(getContext(), "viewpager 错误", Toast.LENGTH_SHORT);
            return false;
        }
    }
}
