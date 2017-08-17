package com.drwang.views.event;

import com.drwang.views.bean.FilterInfo;

/**
 * Created by Administrator on 2017/8/17.
 */

public class FilterChangeEvent {
    public FilterInfo filterInfo;

    public FilterChangeEvent(FilterInfo filterInfo) {
        this.filterInfo = filterInfo;
    }
}
