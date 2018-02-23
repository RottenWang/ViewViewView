package com.drwang.views.dagger;

/**
 * Created by wang on 2018/2/23.
 */

public class RetrofitMananger {
    OkHttpClient okHttpClient;

    public RetrofitMananger(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public void toast() {
        okHttpClient.toast2();
    }
}
