package com.drwang.views.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/7.
 */

public abstract class BasicActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int resId = setContentViewRes();
        setContentView(resId);
        ButterKnife.bind(this);
        initializeView();
        initializeData();
    }

    protected abstract void initializeView();

    protected abstract void initializeData();

    public abstract int setContentViewRes();
}
