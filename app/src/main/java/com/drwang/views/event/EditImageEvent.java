package com.drwang.views.event;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 2017/8/28.
 */

public class EditImageEvent {
    public Bitmap bitmap;

    public EditImageEvent(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
