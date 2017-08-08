package com.drwang.views.bean;

/**
 * Created by Administrator on 2017/8/8.
 */

public class ImageEntityBean {
    public String path;
    public String name;
    public long dateTime;

    public ImageEntityBean(String path, String name, long dateTime) {
        this.path = path;
        this.name = name;
        this.dateTime = dateTime;
    }
}
