package com.drwang.views.dagger;

import com.drwang.views.activity.MainActivity;
import com.drwang.views.activity.SplashActivity;

import dagger.Component;

/**
 * Created by wang on 2018/2/23.
 */
@Component
public interface FactoryActivityComponent {
    void inject(SplashActivity activity);
}
