package com.drwang.views.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.drwang.views.R;
import com.drwang.views.base.BasicActivity;
import com.drwang.views.util.BitmapUtil;
import com.drwang.views.view.PieView;

import butterknife.BindView;

public class PieViewActivity extends BasicActivity implements View.OnClickListener {

    private PieView mPieView;
    @BindView(R.id.iv_alpha)
    ImageView iv_alpha;


    @Override
    protected void initializeView() {
        findViewById(R.id.show_bing).setOnClickListener(this);
        mPieView = (PieView) findViewById(R.id.pieview);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.shadow_origin);
        long t = System.currentTimeMillis();
        Bitmap bitmapEdit = BitmapUtil.generateReSizeBitmapUseMatrix(bitmap, -1, 1);
        long e = System.currentTimeMillis();
        Log.i("a", "initializeView: time = " + (e - t));
        iv_alpha.setImageBitmap(bitmapEdit);
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
