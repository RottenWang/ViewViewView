package com.drwang.views.support;

import android.support.annotation.NonNull;

/**
 * Created by Administrator on 2017/8/8.
 */

public abstract class PriorityRunnable implements Runnable, Comparable<PriorityRunnable> {
    private int priority;

    public PriorityRunnable(int priority) {
        if (priority < 0)
            throw new IllegalArgumentException();

        this.priority = priority;
    }

    @Override
    public int compareTo(@NonNull PriorityRunnable another) {
        int my = this.getPriority();
        int other = another.getPriority();
        return my < other ? 1 : my > other ? -1 : 0;
    }

    public int getPriority() {
        return priority;
    }
}