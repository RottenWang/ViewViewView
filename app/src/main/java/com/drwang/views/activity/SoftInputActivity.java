package com.drwang.views.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.drwang.views.R;
import com.drwang.views.base.BasicActivity;
import com.drwang.views.view.ScanView;
import com.drwang.views.view.TouchView;

import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.OnClick;

public class SoftInputActivity extends BasicActivity {
    @BindView(R.id.rl_root)
    View rl_root;
    private ScanView scan;
    private static final String TAG = "SoftInputActivity";
    @BindView(R.id.touch_view)
    TouchView touch_view;

    @Override
    protected void initializeView() {
        scan = (ScanView) findViewById(R.id.scan);
        scan.setOnZeroListener(() -> Toast.makeText(SoftInputActivity.this, "zerozero", Toast.LENGTH_SHORT).show());
        try {
            Class aClass = ScanView.class;
            Method method2 = aClass.getDeclaredMethod("getRandomColor");
            Method method = aClass.getDeclaredMethod("setTimeStart", int.class);
            method.setAccessible(true);
            method.invoke(scan, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertTheme).setCancelable(true).setTitle("xixi").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNeutralButton("haha", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("22", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.RED);
    }

    @Override
    protected void initializeData() {

    }

    @Override
    public int setContentViewRes() {
        return R.layout.activity_soft_input;
    }

    @OnClick(R.id.tv_restart)
    public void reStart(View v) {
        touch_view.reStart();
    }

}
