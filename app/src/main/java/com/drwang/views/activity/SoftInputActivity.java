package com.drwang.views.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.drwang.views.R;
import com.drwang.views.base.BasicActivity;
import com.drwang.views.util.InputMethodManagerUtil;
import com.drwang.views.view.ScanView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SoftInputActivity extends Activity {
    @BindView(R.id.rl_root)
    View rl_root;
    private ScanView scan;
    private static final String TAG = "SoftInputActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 更改状态栏颜色
        setContentView(R.layout.activity_soft_input);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(android.R.color.holo_blue_bright));

            //底部导航栏
            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
        }
        scan = (ScanView) findViewById(R.id.scan);
        scan.setOnZeroListener(new ScanView.OnZeroListener() {
            @Override
            public void onZero() {
                Toast.makeText(SoftInputActivity.this, "zerozero", Toast.LENGTH_SHORT).show();
            }
        });
        try {
            Class aClass = ScanView.class;
            Method method2 = aClass.getDeclaredMethod("getRandomColor");
            Method method = aClass.getDeclaredMethod("setTimeStart", int.class);
            method.setAccessible(true);
            method.invoke(scan, 100);
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
//        int size = (int) (getResources().getDisplayMetrics().density * 5);
//        ButterKnife.bind(this);
//        rl_root.setOnTouchListener(new View.OnTouchListener() {
//            int startY;
//            int movedY;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        startY = (int) event.getRawY();
//                        return true;
//                    case MotionEvent.ACTION_MOVE:
//                        movedY = (int) event.getRawY();
//                        if (Math.abs(movedY - startY) > size){
//                            finish();
//                        }
//                }
//                return false;
//            }
//        });

    }

}
