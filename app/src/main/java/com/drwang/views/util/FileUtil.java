package com.drwang.views.util;

import android.os.Environment;

import com.drwang.views.App;

/**
 * Created by Administrator on 2017/8/17.
 */

public class FileUtil {
    public static String getFoldPath() {
        String path;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory() + "/image_editer";
        } else {
            path = App.sApplication.getCacheDir() + "/image_editer";
        }
        return path;
    }
}
