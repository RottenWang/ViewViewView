package com.drwang.views.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/8/22.
 */

public class NestedLinearLayout extends LinearLayout implements NestedScrollingParent {
    private static final String TAG = "NestedLinearLayout";
    private View mTopView;
    private int mTopHeight;
    private float damp = 2f; //阻尼系数
    private List<View> mShowViewList;
    private List<View> mHideViewList;
    private float currentHidePercent = 0.5f;
    private float currentShowPercent = 0.5f;

    public NestedLinearLayout(Context context) {
        this(context, null);
    }

    public NestedLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (mShowViewList == null) {
            mShowViewList = new ArrayList<>();
        }
        if (mHideViewList == null) {
            mHideViewList = new ArrayList<>();
        }
    }

    /**
     * 设置阻尼系数  越大则拉动需要的距离越大
     *
     * @param damp
     */
    public void setDamp(float damp) {
        this.damp = damp;
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.i(TAG, ":onStartNestedScroll child = " + child + ",target = " + target + "nestedScrollAxes = " + nestedScrollAxes);
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        Log.i(TAG, ":onNestedScrollAccepted ");
    }

    @Override
    public void onStopNestedScroll(View target) {
        Log.i(TAG, ":onStopNestedScroll ");

    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        Log.i(TAG, ":onNestedScroll ");

    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        //下拉时  dy<0  上拉时 dy>0
        Log.i(TAG, ":onNestedPreScroll dx = " + dx + "dy = " + dy + "consumed = " + consumed);
        boolean hiddenTop = dy > 0 && getScrollY() < mTopHeight;
        boolean showTop = dy < 0 && getScrollY() > 0 && !ViewCompat.canScrollVertically(target, -1);
        if (hiddenTop || showTop) {
            dy = (int) Math.ceil(dy / damp);

            /*-----------------------限制滑动范围 防止快速拖动时,造成的越界-----------------------*/
            int scrollY = getScrollY();
            if (showTop && scrollY + dy < 0) {
                dy = -scrollY;
            }
            if (hiddenTop && scrollY + dy > mTopHeight) {
                dy = mTopHeight - scrollY;
            }
            /*------------------------------------------------------------------------------------*/
            scrollBy(0, dy);
            int dyY = dy;
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            layoutParams.height = layoutParams.height + dyY;
            setLayoutParams(layoutParams);
            consumed[1] = dy;
            float percent = 1 - scrollY / (mTopHeight * 1.0f);
            showOrHideView(percent);

            mTopView.setAlpha(percent);

        }
    }

    private void showOrHideView(float percent) {
        for (int i = 0; i < mShowViewList.size(); i++) {
            View view = mShowViewList.get(i);
            if (view != null) {
                if (percent != 0) {
                    view.setVisibility(View.VISIBLE);
                }
                if (percent >= currentShowPercent) {
                    view.setVisibility(View.INVISIBLE);
                }
                view.setAlpha(1 - percent);
            }
        }
        for (int i = 0; i < mHideViewList.size(); i++) {
            View view = mHideViewList.get(i);
            if (view != null) {
                if (percent != 1 && percent > currentHidePercent) {
                    view.setVisibility(View.VISIBLE);
                }
                if (percent <= currentHidePercent) {
                    view.setVisibility(View.INVISIBLE);
                }
                view.setAlpha(percent);
            }
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        Log.i(TAG, ":onNestedFling ");
        return false;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        Log.i(TAG, ":onNestedPreFling ");
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        Log.i(TAG, ":onNestedPreFling ");
        return 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopHeight = mTopView.getMeasuredHeight();
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = getMeasuredHeight();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTopView = getChildAt(0);
    }

    /**
     * 关联到滑动布局 在隐藏头部时显示
     *
     * @param view
     */
    public void addToShowViewList(View view) {
        mShowViewList.add(view);
    }

    /**
     * 取消关联到滑动布局{@link NestedLinearLayout#addToShowViewList(View)}}
     */
    public void removeShowList(View view) {
        mShowViewList.remove(view);
    }

    /**
     * 关联到滑动布局 在隐藏头部时隐藏
     *
     * @param view
     */
    public void addToHideViewList(View view) {
        mHideViewList.add(view);
    }

    /**
     * 取消关联到滑动布局 {@link NestedLinearLayout#addToHideViewList(View)}
     */
    public void removeHideViewList(View view) {
        mHideViewList.remove(view);
    }

    /**
     * 设置隐藏布局百分比 range 0-1f
     *
     * @param hidePercent 值越大 隐藏头部布局时越容易隐藏在mHideViewList中的View 默认0.5f
     */
    public void setHidePercent(float hidePercent) {
        this.currentHidePercent = hidePercent < 0 ? 0 : hidePercent > 1 ? 1 : hidePercent;
    }

    /**
     * 设置显示布局百分比  range 0-1f
     *
     * @param showPercent 值越大 隐藏头部布局时越容易显示在mShowViewList中的View  默认0.5f
     */
    public void setShowPercent(float showPercent) {
        this.currentShowPercent = showPercent < 0 ? 0 : showPercent > 1 ? 1 : showPercent;
    }
}
