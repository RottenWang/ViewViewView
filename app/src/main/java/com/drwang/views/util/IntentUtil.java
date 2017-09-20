package com.drwang.views.util;

import android.app.Activity;
import android.content.Intent;

import com.drwang.views.activity.DecorationActivity;
import com.drwang.views.activity.EditImageActivity;
import com.drwang.views.activity.FilterActivity;
import com.drwang.views.activity.ImageActivity;
import com.drwang.views.activity.ImagePreviewActivity;
import com.drwang.views.activity.PaintActivity;
import com.drwang.views.activity.PieViewActivity;
import com.drwang.views.activity.RecyclerActivity;
import com.drwang.views.activity.SelectTitleImageActivity;
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
        Intent intent = new Intent(activity, ShadowActivity.class);
        activity.startActivity(intent);
    }

    public static void toImageActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, ImageActivity.class);
        activity.startActivity(intent);
    }

    public static void toImagePreviewActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, ImagePreviewActivity.class);
        activity.startActivity(intent);
    }

    public static void toDecorationActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, DecorationActivity.class);
        activity.startActivity(intent);
    }

    public static void toFilterActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, FilterActivity.class);
        activity.startActivity(intent);
    }

    public static void toEditImageActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, EditImageActivity.class);
        activity.startActivity(intent);
    }

    public static void toSelectTitleImageActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        Intent intent = new Intent(activity, SelectTitleImageActivity.class);
        activity.startActivity(intent);
    }
}
