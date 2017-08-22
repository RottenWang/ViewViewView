package com.drwang.views.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
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
//        exampleAdapter.setHeaderView(headerView);
//        exampleAdapter.setFooterView(footerView);
        if (layoutManager == null)
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
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
}
