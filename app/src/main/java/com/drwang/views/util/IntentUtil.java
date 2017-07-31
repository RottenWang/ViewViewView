package com.drwang.views.util;

import android.app.Activity;
import android.content.Intent;

import com.drwang.views.activity.MainActivity;
import com.drwang.views.activity.PaintActivity;
import com.drwang.views.activity.PieViewActivity;
import com.drwang.views.activity.RecyclerActivity;
import com.drwang.views.activity.ShadowActivity;
import com.drwang.views.activity.SoftInputActivity;

/**
 * Created by Administrator on 2017/7/7.
 */

public class IntentUtil {
    private IntentUtil() {
    }


    public static void toPieViewActivity(Activity activity) {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, PieViewActivity.class);
        activity.startActivity(intent);
    }

    public static void toPaintActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, PaintActivity.class);
        activity.startActivity(intent);
    }

    public static void toRecyclerViewActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, RecyclerActivity.class);
        activity.startActivity(intent);
    }

    public static void toSoftInputActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, SoftInputActivity.class);
        activity.startActivity(intent);
    }

    public static void toShadowActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity,ShadowActivity.class);
        activity.startActivity(intent);
    }
}
