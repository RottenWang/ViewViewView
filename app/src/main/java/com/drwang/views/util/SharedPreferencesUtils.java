package com.drwang.views.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.drwang.views.App;

/**
 * Created by Administrator on 2017/8/8.
 */

public class SharedPreferencesUtils {
    public static final String PRE_KEY = "dr_wang_image_local";
    public static SharedPreferences sharedPreferences;

    private SharedPreferencesUtils() {
    }

    private static SharedPreferences getInstance() {
        if (sharedPreferences == null) {
            synchronized (SharedPreferencesUtils.class) {
                if (sharedPreferences == null) {
                    sharedPreferences = App.sApplication.getSharedPreferences(PRE_KEY, Context.MODE_PRIVATE);
                }
            }
        }
        return sharedPreferences;
    }

    public static void putString(String key, String value) {
        getInstance().edit().putString(key, value).apply();
    }

    public static String getString(String key, String defaultValue) {
        return getInstance().getString(key, defaultValue);
    }

    public static void putInt(String key, int value) {
        getInstance().edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int defaultValue) {
        return getInstance().getInt(key, defaultValue);
    }

    public static void putLong(String key, long value) {
        getInstance().edit().putLong(key, value).apply();
    }

    public static long getLong(String key, long defaultValue) {
        return getInstance().getLong(key, defaultValue);
    }
}
