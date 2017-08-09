package com.drwang.views.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.drwang.views.R;
import com.drwang.views.adapter.PhotoViewPagerAdpter;
import com.drwang.views.base.BasicActivity;
import com.drwang.views.bean.ImageEntityBean;
import com.drwang.views.support.fresco.FrescoScheme;
import com.drwang.views.support.fresco.FrescoUtils;
import com.drwang.views.view.PhotoViewPager;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.relex.photodraweeview.PhotoDraweeView;

public class ImagePreviewActivity extends BasicActivity {
    @BindView(R.id.photo_viewpager)
    PhotoViewPager photo_viewpager;
    public static final String PHOTO_INFO = "PHOTO_INFO";
    List<ImageEntityBean> mImageEntityBeanList;
    private PhotoViewPagerAdpter photoViewPagerAdpter;

    @Override
    protected void initializeView() {
    }

    @Override
    protected void initializeData() {
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        setStatusBarDarkMode(false, this);
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra(PHOTO_INFO);
            if (bundle != null) {
                mImageEntityBeanList = (ArrayList<ImageEntityBean>) bundle.getSerializable(PHOTO_INFO);
            }
        }
        if (photoViewPagerAdpter == null) {
            photoViewPagerAdpter = new PhotoViewPagerAdpter(this, mImageEntityBeanList);
        }
        photo_viewpager.setAdapter(photoViewPagerAdpter);
        photo_viewpager.setOffscreenPageLimit(10);


    }

    boolean isHide = true;

    @OnClick(R.id.btn)
    public void click(View v) {
        if (isHide) {
            if (Build.VERSION.SDK_INT >= 19) {
                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        } else {
            if (Build.VERSION.SDK_INT >= 19) {
                View decorView = getWindow().getDecorView();
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
        isHide = !isHide;
    }

    @Override
    public int setContentViewRes() {
        return R.layout.activity_image_preview;
    }

}
