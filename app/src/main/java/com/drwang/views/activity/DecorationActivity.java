package com.drwang.views.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.drwang.views.R;
import com.drwang.views.adapter.ExampleAdapter;
import com.drwang.views.base.BasicActivity;
import com.drwang.views.decoration.CustomerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DecorationActivity extends BasicActivity {
    @BindView(R.id.recyclerview_decoration)
    RecyclerView recyclerView;
    List<String> mList;
    private ExampleAdapter exampleAdapter;

    @Override
    protected void initializeView() {
        LinearLayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
        recyclerView.addItemDecoration(new CustomerItemDecoration(this));
        mList = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            mList.add(i + "");
        }
        exampleAdapter = new ExampleAdapter(this, mList);
        recyclerView.setAdapter(exampleAdapter);
    }

    @Override
    protected void initializeData() {

    }

    @Override
    public int setContentViewRes() {
        return R.layout.activity_decoration;
    }
}
