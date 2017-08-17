package com.drwang.views.bean;

import com.drwang.views.event.RefreshGPUImageEvent;

import org.greenrobot.eventbus.EventBus;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

/**
 * Created by Administrator on 2017/8/17.
 */

public class FilterInfo {
    public GPUImageFilter filter;
    public String filterName;
    public boolean hasRange;
    public float rangeMix;
    public float progress;
    public float value;
    GPUImageChangeInterface<GPUImageFilter> listener;

    public FilterInfo(GPUImageFilter filter, String filterName, float currentRange, float rangeMix, float rangeMax, GPUImageChangeInterface<GPUImageFilter> listener) {
        this.filter = filter;
        this.filterName = filterName;
        this.progress = currentRange / rangeMax;
        this.hasRange = true;
        this.rangeMix = rangeMix;
        this.rangeMax = rangeMax;
        this.listener = listener;
    }

    public FilterInfo(GPUImageFilter filter, String filterName) {
        this.filter = filter;
        this.filterName = filterName;
    }

    public float rangeMax;

    public void setProgress(float progress) {
        if (!hasRange) {
            return;
        }
        this.progress = progress;
        value = rangeMax * progress;
        if (listener != null) {
            listener.setProgress(value, filter);
        }
        EventBus.getDefault().post(new RefreshGPUImageEvent());
    }

    public interface GPUImageChangeInterface<T> {
        void setProgress(float progress, T filter);
    }


}
