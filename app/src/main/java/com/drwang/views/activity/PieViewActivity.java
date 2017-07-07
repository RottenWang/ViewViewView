package com.drwang.views.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.drwang.views.R;
import com.drwang.views.view.PieView;

public class PieViewActivity extends AppCompatActivity implements View.OnClickListener {

    private PieView mPieView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        findViewById(R.id.show_bing).setOnClickListener(this);
        mPieView = (PieView) findViewById(R.id.pieview);
    }

    private static final String TAG = "PieViewActivity";

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
