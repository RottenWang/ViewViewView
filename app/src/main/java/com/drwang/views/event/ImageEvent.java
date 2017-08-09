package com.drwang.views.event;

import com.drwang.views.bean.ImageEntityBean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/9.
 */

public class ImageEvent {

    public List<ImageEntityBean> mList;
    public int position;

    public ImageEvent(List<ImageEntityBean> mList, int position) {
        this.mList = mList;
        this.position = position;
    }
}
