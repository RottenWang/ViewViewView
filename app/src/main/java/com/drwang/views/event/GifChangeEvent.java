package com.drwang.views.event;

import com.drwang.views.bean.ImageEntityBean;

/**
 * Created by Administrator on 2017/8/31.
 */

public class GifChangeEvent {
    public ImageEntityBean imageEntityBean;

    public GifChangeEvent(ImageEntityBean imageEntityBean) {
        this.imageEntityBean = imageEntityBean;
    }
}
