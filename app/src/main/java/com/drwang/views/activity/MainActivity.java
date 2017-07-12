package com.drwang.views.activity;

import android.view.View;
import android.widget.TextView;

import com.drwang.views.R;
import com.drwang.views.base.BasicActivity;
import com.drwang.views.kotlin.KotlinBase_1;
import com.drwang.views.util.IntentUtil;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BasicActivity {

    @BindView(R.id.tv_pieview)
    TextView tv_pieview;
    private KotlinBase_1 kotlinBase_1;


    @OnClick({R.id.tv_inObj, R.id.tv_diedai, R.id.tv_pieview, R.id.tv_printSum, R.id.tv_printFor, R.id.tv_printWhile, R.id.tv_printWhen, R.id.tv_inin, R.id.tv_out})
    public void toOtherActivity(View view) {
        switch (view.getId()) {
            case R.id.tv_pieview:
                IntentUtil.toPieViewActivity(this);
                break;
            case R.id.tv_printSum:
                kotlinBase_1.printInt("a", "b");
                break;
            case R.id.tv_printFor:
                kotlinBase_1.forFor();
                break;
            case R.id.tv_printWhile:
                kotlinBase_1.whileWhile();
                break;
            case R.id.tv_printWhen:
                kotlinBase_1.whenWhen();
                break;
            case R.id.tv_inin:
                kotlinBase_1.inIn();
                break;
            case R.id.tv_out:
                kotlinBase_1.outOut();
                break;
            case R.id.tv_diedai:
                kotlinBase_1.diedai();
                break;
            case R.id.tv_inObj:
                kotlinBase_1.inObj();
                break;
        }
    }

    @Override
    public int setContentViewRes() {
        return R.layout.activity_main;
    }


    @Override
    protected void initializeView() {
        kotlinBase_1 = new KotlinBase_1();
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
