package com.drwang.views.util;

import android.app.Activity;
import android.content.Intent;

import com.drwang.views.activity.PieViewActivity;

/**
 * Created by Administrator on 2017/7/7.
 */

public class IntentUtil {
    private IntentUtil() {
    }


    public static void toPieViewActivity(Activity activity) {
        if (activity == null){
            return;
             }

        Intent intent = new Intent(activity, PieViewActivity.class);
        activity.startActivity(intent);
    }

}
