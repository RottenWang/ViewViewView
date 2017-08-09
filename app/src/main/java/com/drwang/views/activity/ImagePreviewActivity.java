package com.drwang.views.activity;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.drwang.views.R;
import com.drwang.views.adapter.PhotoViewPagerAdapter;
import com.drwang.views.base.BasicActivity;
import com.drwang.views.bean.ImageEntityBean;
import com.drwang.views.event.ImageEvent;
import com.drwang.views.event.ShowOrHideEvent;
import com.drwang.views.util.AnimationUtil;
import com.drwang.views.view.PhotoViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;

public class ImagePreviewActivity extends BasicActivity {
    @BindView(R.id.photo_viewpager)
    PhotoViewPager photo_viewpager;
    @BindView(R.id.rl_bottom)
    View rl_bottom;
    @BindView(R.id.rl_title)
    View rl_title;
    List<ImageEntityBean> mImageEntityBeanList;
    private PhotoViewPagerAdapter photoViewPagerAdapter;
    float density;
    private int statusBarHeight;

    @Override
    protected void initializeView() {
        density = getResources().getDisplayMetrics().density;
        statusBarHeight = getStatusBarHeight();
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) rl_title.getLayoutParams();
        layoutParams.height = (int) (50 * density + statusBarHeight + 0.5f);
        layoutParams.topMargin = -(int) (50 * density + statusBarHeight + 0.5f);
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


    @Subscribe
    public void ShowOrHideEvent(ShowOrHideEvent showOrHideEvent) {
        if (showOrHideEvent.isHide) {
            //去显示
            AnimationUtil.marginBottomTranslateAnimation(rl_bottom, 0);
            AnimationUtil.marginTopTranslateAnimation(rl_title, 0);
        } else {
            //去隐藏
            AnimationUtil.marginBottomTranslateAnimation(rl_bottom, -80 * density);
            AnimationUtil.marginTopTranslateAnimation(rl_title, -(50 * density + statusBarHeight + 0.5f));
        }
    }
}
