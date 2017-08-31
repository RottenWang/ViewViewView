package com.drwang.views.event;

import com.drwang.views.bean.ImageEntityBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/31.
 */

public class GifImageInfoEvent {
    public ArrayList<ImageEntityBean> gifInfoList;

    public GifImageInfoEvent(ArrayList<ImageEntityBean> gifInfoList) {
        this.gifInfoList = gifInfoList;
    }
}
