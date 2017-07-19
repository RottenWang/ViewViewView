package com.drwang.views.activity;

import android.app.Activity;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.drwang.views.R;
import com.drwang.views.base.BasicActivity;
import com.drwang.views.util.InputMethodManagerUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SoftInputActivity extends Activity {
    @BindView(R.id.rl_root)
    View rl_root;

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
        int size = (int) (getResources().getDisplayMetrics().density * 5);
        ButterKnife.bind(this);
        rl_root.setOnTouchListener(new View.OnTouchListener() {
            int startY;
            int movedY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startY = (int) event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        movedY = (int) event.getRawY();
                        if (Math.abs(movedY - startY) > size){
                            finish();
                        }
                }
                return false;
            }
        });

    }

}
