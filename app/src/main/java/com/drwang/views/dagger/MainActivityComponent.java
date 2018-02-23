package com.drwang.views.dagger;

import android.app.Activity;

import com.drwang.views.activity.MainActivity;

import dagger.Component;

/**
 * Created by wang on 2018/2/23.
 */

@Component(modules = MainActivityModule.class)
public interface MainActivityComponent {
    void inject(MainActivity activity);
}
