package com.drwang.views.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/8/8.
 */

public class ImageEntityBean implements Parcelable {
    public String path;
    public String name;

    public ImageEntityBean(String path, String name, long dateTime, int width, int height) {
        this.path = path;
        this.name = name;
        this.dateTime = dateTime;
        this.width = width;
        this.height = height;
    }

    public long dateTime;
    public int width;
    public int height;

    protected ImageEntityBean(Parcel in) {
        path = in.readString();
        name = in.readString();
        dateTime = in.readLong();
        width = in.readInt();
        height = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeString(name);
        dest.writeLong(dateTime);
        dest.writeInt(width);
        dest.writeInt(height);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImageEntityBean> CREATOR = new Creator<ImageEntityBean>() {
        @Override
        public ImageEntityBean createFromParcel(Parcel in) {
            return new ImageEntityBean(in);
        }

        @Override
        public ImageEntityBean[] newArray(int size) {
            return new ImageEntityBean[size];
        }
    };
}
