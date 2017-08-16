package com.drwang.views.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.drwang.views.R;
import com.drwang.views.base.BasicActivity;
import com.drwang.views.view.PieView;

public class PieViewActivity extends BasicActivity implements View.OnClickListener {

    private PieView mPieView;


    @Override
    protected void initializeView() {
        findViewById(R.id.show_bing).setOnClickListener(this);
        mPieView = (PieView) findViewById(R.id.pieview);
    }

    @Override
    protected void initializeData() {

    }

    @Override
    public int setContentViewRes() {
        return R.layout.activity_view;
    }

    @Override
    public void onClick(View v) {
        mPieView.addData("haha2", 900);
        mPieView.addData("haha3", 800);
        mPieView.addData("haha4", 500);
        mPieView.addData("haha", 3000);
        mPieView.addData("haha", 2090);
        mPieView.addData("haha", 3300);
        mPieView.addData("haha2", 900);
        mPieView.addData("haha2", 900);
        mPieView.startDraw();
    }
}
