package com.drwang.views.dagger;

import android.widget.Toast;

import com.drwang.views.App;

import javax.inject.Inject;

/**
 * Created by wang on 2018/2/23.
 */

public class OkHttpClient {
    String text = "haha";

    public OkHttpClient() {
    }

    public void toast2() {
        Toast.makeText(App.sApplication, text, Toast.LENGTH_LONG).show();
    }

    public void setText(String text){
        this.text = text;
    }


}
