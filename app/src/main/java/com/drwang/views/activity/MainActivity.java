package com.drwang.views.activity;

import android.view.View;
import android.widget.TextView;

import com.drwang.views.R;
import com.drwang.views.base.BasicActivity;
import com.drwang.views.util.IntentUtil;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BasicActivity {

    @BindView(R.id.tv_pieview)
    TextView tv_pieview;



    @OnClick({R.id.tv_pieview})
    public void toOtherActivity(View view) {
        switch (view.getId()) {
            case R.id.tv_pieview:
                IntentUtil.toPieViewActivity(this);
                break;
        }
    }

    @Override
    public int setContentViewRes() {
        return R.layout.activity_main;
    }


    @Override
    protected void initializeView() {
//        ButterKnife.bind(this);
//        findViewById(R.id.tv_pieview).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                IntentUtil.toPieViewActivity(MainActivity.this);
//            }
//        });
    }

    @Override
    protected void initializeData() {

    }

}
