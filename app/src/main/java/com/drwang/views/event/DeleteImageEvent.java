package com.drwang.views.event;

import com.drwang.views.bean.ImageEntityBean;

/**
 * Created by Administrator on 2017/8/9.
 */

public class DeleteImageEvent {
    public ImageEntityBean imageEntityBean;

    public DeleteImageEvent(ImageEntityBean imageEntityBean) {

        this.imageEntityBean = imageEntityBean;
    }
}
