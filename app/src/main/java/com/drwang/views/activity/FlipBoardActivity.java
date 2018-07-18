package com.drwang.views.activity;

import android.graphics.BitmapFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.drwang.views.R;
import com.drwang.views.adapter.ExampleAdapter;
import com.drwang.views.base.BasicActivity;
import com.drwang.views.decoration.CustomerItemDecoration;
import com.drwang.views.view.FlipBoardView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FlipBoardActivity extends BasicActivity {
    @BindView(R.id.fbv_1)
    FlipBoardView fbv_1;
    @BindView(R.id.fbv_2)
    FlipBoardView fbv_2;

    @Override
    protected void initializeView() {
        fbv_1.setStartDegree(90);
        fbv_1.setBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.bg_rectangle));
        fbv_2.setStartDegree(180);
        fbv_2.setBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.bg_rectangle2));

    }

    @Override
    protected void initializeData() {

    }

    @Override
    public int setContentViewRes() {
        return R.layout.activity_flipboard;
    }
}
