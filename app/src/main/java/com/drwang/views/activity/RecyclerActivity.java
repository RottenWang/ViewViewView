package com.drwang.views.activity;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PointF;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.drwang.views.R;
import com.drwang.views.adapter.ExampleAdapter;
import com.drwang.views.base.BasicActivity;
import com.drwang.views.view.NestedLinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

public class RecyclerActivity extends BasicActivity {
    @BindView(R.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.tv_title2)
    View tv_title2;
    @BindView(R.id.tv_title3)
    View tv_title3;
    @BindView(R.id.nll_root)
    NestedLinearLayout nll_root;
    ExampleAdapter exampleAdapter;
    List<String> mList;
    LinearLayoutManager layoutManager;
    GridLayoutManager gridLayoutManager;


    @Override
    protected void initializeView() {

    }

    @Override
    protected void initializeData() {
        if (mList == null) {
            mList = new ArrayList<>();
            mList.add("ee");
        }
        if (exampleAdapter == null)
            exampleAdapter = new ExampleAdapter(this, mList);
        View headerView = LayoutInflater.from(this).inflate(R.layout.layout_text, null);
        View footerView = LayoutInflater.from(this).inflate(R.layout.layout_text, null);
        TextView textView = (TextView) headerView.findViewById(R.id.tv_test);
        textView.setText("this is header");
        TextView textView2 = (TextView) footerView.findViewById(R.id.tv_test);
        textView2.setText("this is footer");
        exampleAdapter.setHeaderView(headerView);
//        exampleAdapter.setFooterView(footerView);
        if (layoutManager == null)
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        if (gridLayoutManager == null) {
            gridLayoutManager = new MineGridLayoutManager(this, 3);
        }
        gridLayoutManager.setSpanSizeLookup(new SpanSize());
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(exampleAdapter);
        nll_root.addToShowViewList(tv_title2);
        nll_root.addToHideViewList(tv_title3);
    }

    @Override
    public int setContentViewRes() {
        return R.layout.activity_recycler;
    }

    Random random = new Random();

    @OnClick(R.id.bt_add)
    public void add(View v) {
        mList.add(String.valueOf(random.nextInt(1000)));
        exampleAdapter.notifyDataSetChanged();
//        exampleAdapter.notifyItemInserted(0);
//        exampleAdapter.notifyItemRangeChanged(0, mList.size()-1);
        recyclerView.scrollToPosition(mList.size() - 1);
//        recyclerView.smoothScrollToPosition(mList.size() - 1);
    }

    private static class MineGridLayoutManager extends GridLayoutManager {

        private float MILLISECONDS_PER_INCH = 0.03f;
        private Context mContext;

        public MineGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            mContext = context;
        }

        public MineGridLayoutManager(Context context, int spanCount) {
            super(context, spanCount);
            mContext = context;
        }

        public MineGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
            super(context, spanCount, orientation, reverseLayout);
            mContext = context;
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
//            super.smoothScrollToPosition(recyclerView, state, position);
            LinearSmoothScroller linearSmoothScroller =
                    new LinearSmoothScroller(recyclerView.getContext()) {
                        @Override
                        public PointF computeScrollVectorForPosition(int targetPosition) {
                            return MineGridLayoutManager.this
                                    .computeScrollVectorForPosition(targetPosition);
                        }

                        //This returns the milliseconds it takes to
                        //scroll one pixel.
                        @Override
                        protected float calculateSpeedPerPixel
                        (DisplayMetrics displayMetrics) {
                            return MILLISECONDS_PER_INCH / displayMetrics.density;
                            //返回滑动一个pixel需要多少毫秒
                        }

                    };
            linearSmoothScroller.setTargetPosition(position);
            startSmoothScroll(linearSmoothScroller);
        }

        public void setSpeedSlow() {
            //自己在这里用density去乘，希望不同分辨率设备上滑动速度相同
            //0.3f是自己估摸的一个值，可以根据不同需求自己修改
            MILLISECONDS_PER_INCH = mContext.getResources().getDisplayMetrics().density * 0.3f;
        }

        public void setSpeedFast() {
            MILLISECONDS_PER_INCH = mContext.getResources().getDisplayMetrics().density * 0.03f;
        }

    }

    public class SpanSize extends GridLayoutManager.SpanSizeLookup {

        @Override
        public int getSpanSize(int position) {
            if (position == 0) {
                return 3;
            } else {
                return 1;
            }
        }
    }
}
