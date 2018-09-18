package com.drwang.views.test;

import android.util.Log;


public class SynchronizedTest {

    private int a = 0;

    public synchronized void a() {
        for (int i = 0; i < 1000000; i++) {
            int ii = ++a;
            if (ii != a) {
                Log.d("wangchen2", "ii = " + ii + ",i = " + a);
            }
        }
        Log.d("wangchen2", "finish i = " + a);
    }

    public void aa() {
        synchronized (SynchronizedTest.class) {
            for (int i = 0; i < 1000000; i++) {
                int ii = ++a;
                if (ii != a) {
                    Log.d("wangchen2", "ii = " + ii + ",i = " + a);
                }
            }
            Log.d("wangchen2", "finish i = " + a);
        }
    }


}
