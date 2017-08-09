package com.drwang.views.activity;

import android.os.Build;
import android.view.View;

import com.drwang.views.R;
import com.drwang.views.adapter.PhotoViewPagerAdapter;
import com.drwang.views.base.BasicActivity;
import com.drwang.views.bean.ImageEntityBean;
import com.drwang.views.event.ImageEvent;
import com.drwang.views.view.PhotoViewPager;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;

public class ImagePreviewActivity extends BasicActivity {
    @BindView(R.id.photo_viewpager)
    PhotoViewPager photo_viewpager;
    List<ImageEntityBean> mImageEntityBeanList;
    private PhotoViewPagerAdapter photoViewPagerAdapter;

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
        ImageEvent stickyEvent = EventBus.getDefault().getStickyEvent(ImageEvent.class);
        EventBus.getDefault().removeStickyEvent(stickyEvent);
        mImageEntityBeanList = stickyEvent.mList;

        if (photoViewPagerAdapter == null) {
            photoViewPagerAdapter = new PhotoViewPagerAdapter(this, mImageEntityBeanList);
        }
        photo_viewpager.setAdapter(photoViewPagerAdapter);
        photo_viewpager.setOffscreenPageLimit(10);
        photo_viewpager.setCurrentItem(stickyEvent.position, false);

    }


    @Override
    public int setContentViewRes() {
        return R.layout.activity_image_preview;
    }

}
