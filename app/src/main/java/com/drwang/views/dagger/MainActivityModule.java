package com.drwang.views.dagger;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wang on 2018/2/23.
 */
@Module
public class MainActivityModule {
    String text;

    public MainActivityModule(String toast) {
        text = toast;
    }

    @Provides
    OkHttpClient provideOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setText(text);
        return okHttpClient;
    }

    @Provides
    RetrofitMananger provideRetrofitManager(OkHttpClient client) {
        return new RetrofitMananger(client);
    }

}
