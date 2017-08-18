package com.drwang.views.support;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/8/8.
 */

public class LocalThreadPoolManager {


    private LocalThreadPoolExecutor mLocalThreadPoolExecutor;
    int CPUCount;
    int corePoolSize;
    int maximumPoolSize;
    private static LocalThreadPoolManager sLocalThreadPoolManager;

    public LocalThreadPoolManager() {
        CPUCount = Runtime.getRuntime().availableProcessors();
        corePoolSize = CPUCount - 1 > 0 ? 2 : 1;
        maximumPoolSize = CPUCount * 2 + 1;
        mLocalThreadPoolExecutor = new LocalThreadPoolExecutor(corePoolSize, maximumPoolSize, 0L, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>());

    }

    private static LocalThreadPoolManager getInstance() {
        if (sLocalThreadPoolManager == null) {
            sLocalThreadPoolManager = new LocalThreadPoolManager();
        }
        return sLocalThreadPoolManager;
    }

    private LocalThreadPoolExecutor getExecutor() {
        if (mLocalThreadPoolExecutor == null) {
            mLocalThreadPoolExecutor = new LocalThreadPoolExecutor(corePoolSize, maximumPoolSize, 0L, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>());
        }
        return mLocalThreadPoolExecutor;
    }

    public static void execute(PriorityRunnable runnable) {
        LocalThreadPoolManager.getInstance().getExecutor().execute(runnable);
    }

}
