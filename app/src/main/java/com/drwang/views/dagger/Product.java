package com.drwang.views.dagger;

import android.widget.Toast;

import com.drwang.views.App;

import javax.inject.Inject;

/**
 * Created by wang on 2018/2/23.
 */
public class Product {
    @Inject
    public Product() {
    }


    public void toast() {
        Toast.makeText(App.sApplication, "哈哈2", Toast.LENGTH_LONG).show();
    }
}
