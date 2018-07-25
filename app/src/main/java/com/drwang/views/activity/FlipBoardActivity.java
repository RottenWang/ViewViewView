package com.drwang.views.activity;

import android.graphics.BitmapFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.drwang.views.R;
import com.drwang.views.adapter.ExampleAdapter;
import com.drwang.views.base.BasicActivity;
import com.drwang.views.decoration.CustomerItemDecoration;
import com.drwang.views.view.FlipBoardView;
import com.drwang.views.view.TagLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FlipBoardActivity extends BasicActivity {
    @BindView(R.id.tagLayout)
    TagLayout tagLayout;
    @Override
    protected void initializeView() {

    }

    @Override
    protected void initializeData() {

    }

    @Override
    public int setContentViewRes() {
        return R.layout.activity_flipboard;
    }
}
