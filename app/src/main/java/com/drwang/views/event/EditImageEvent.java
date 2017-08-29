package com.drwang.views.event;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/8/28.
 */

public class EditImageEvent {
    public Bitmap bitmap;
    public String name;

    public EditImageEvent(Bitmap bitmap,String name) {
        this.bitmap = bitmap;
        this.name = name;
    }
}
