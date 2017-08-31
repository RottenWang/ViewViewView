package com.drwang.views.event;

import android.graphics.Bitmap;

import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by Administrator on 2017/8/28.
 */

public class EditImageEvent {
    public Bitmap bitmap;
    public String name;
    public String path;
    public GPUImageFilter filter;
    public float scale;

    public EditImageEvent(Bitmap bitmap, String name, String path, GPUImageFilter filter, float scale) {
        this.bitmap = bitmap;
        this.name = name;
        this.path = path;
        this.filter = filter;
        this.scale = scale;
    }
}
