package com.drwang.views.util;

import android.content.Context;

/**
 * Created by Administrator on 2017/8/8.
 */

public class DensityUtil {

    private DensityUtil() {
    }

    private static DensityUtil sDensityUtil;

    public static DensityUtil getInstance() {
        if (sDensityUtil == null) {
            synchronized (DensityUtil.class) {
                if (sDensityUtil == null) {
                    sDensityUtil = new DensityUtil();
                }
            }
        }
        return sDensityUtil;
    }

    public float getScreenWidth(Context context){
        return context.getApplicationContext().getResources().getDisplayMetrics().widthPixels;
    }

    public float getDensity(Context context){
        return context.getApplicationContext().getResources().getDisplayMetrics().density;
    }
    public float getScreenHeight(Context context){
        return context.getApplicationContext().getResources().getDisplayMetrics().heightPixels;
    }
}
