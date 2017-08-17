package com.drwang.views.activity;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.drwang.views.R;
import com.drwang.views.adapter.PhotoViewPagerAdapter;
import com.drwang.views.base.BasicActivity;
import com.drwang.views.bean.ImageEntityBean;
import com.drwang.views.event.DeleteImageEvent;
import com.drwang.views.event.ImageEvent;
import com.drwang.views.event.ImageScaleEvent;
import com.drwang.views.event.ShowOrHideEvent;
import com.drwang.views.util.AnimationUtil;
import com.drwang.views.util.IntentUtil;
import com.drwang.views.view.CircleBgImageView;
import com.drwang.views.view.PhotoViewPager;
import com.facebook.drawee.backends.pipeline.Fresco;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;

public class ImagePreviewActivity extends BasicActivity {
    @BindView(R.id.photo_viewpager)
    PhotoViewPager photo_viewpager;
    @BindView(R.id.rl_bottom)
    View rl_bottom;
    @BindView(R.id.rl_title)
    View rl_title;
    @BindView(R.id.civ_share)
    CircleBgImageView civ_share;
    List<ImageEntityBean> mImageEntityBeanList;
    private PhotoViewPagerAdapter photoViewPagerAdapter;
    float density;
    private int statusBarHeight;
    @BindView(R.id.rl_root)
    View rl_root;
    @BindView(R.id.tv_date)
    TextView tv_date;

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
        if (stickyEvent != null) {
            EventBus.getDefault().removeStickyEvent(stickyEvent);
            mImageEntityBeanList = stickyEvent.mList;
        }

        if (photoViewPagerAdapter == null) {
            photoViewPagerAdapter = new PhotoViewPagerAdapter(this, mImageEntityBeanList);
        }
        photo_viewpager.setAdapter(photoViewPagerAdapter);
        photo_viewpager.setOffscreenPageLimit(3);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        photoViewPagerAdapter.onDestroy();
    }

    private boolean isCuteMode = false;

    @OnClick(R.id.civ_share)
    public void clickCiv(View v) {
        ImageEntityBean imageEntityBean = mImageEntityBeanList.get(photo_viewpager.getCurrentItem());
        long dateTime = imageEntityBean.dateTime;
        String format = new SimpleDateFormat("yyyy年MM月dd日|HH:mm", Locale.CHINA).format(new Date(dateTime * 1000));
        String[] split = format.split("\\|");
        tv_date.setText(split[0]);
        isCuteMode = true;
        int currentItem = photo_viewpager.getCurrentItem();
        EventBus.getDefault().post(new ImageScaleEvent(false));
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f, 0.9f);
        valueAnimator.addUpdateListener((value) -> {
            float animatedValue = (float) value.getAnimatedValue();
            Log.i("animatedValue", "clickCiv: animatedValue = " + animatedValue);
            photo_viewpager.setScaleX(animatedValue);
            photo_viewpager.setScaleY(animatedValue);

        });
        rl_root.setClickable(true);
        valueAnimator.setDuration(100);
        valueAnimator.start();
        AnimationUtil.marginBottomTranslateAnimation(rl_bottom, -80 * density);
        AnimationUtil.marginTopTranslateAnimation(rl_title, -(50 * density + statusBarHeight + 0.5f));
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

    @OnClick(R.id.civ_edit)
    public void editImage(View v) {
        EventBus.getDefault().postSticky(new ImageEvent(mImageEntityBeanList, photo_viewpager.getCurrentItem()));
        IntentUtil.toFilterActivity(this);
    }

    @OnClick(R.id.civ_delete)
    public void deleteImage(View v) {
        showDeleteDialog(photo_viewpager.getCurrentItem());
    }

    @Override
    public void onBackPressed() {
        if (isCuteMode) {
            rl_root.setOnClickListener(null);
            rl_root.setClickable(false);
            isCuteMode = false;
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.9f, 1.0f);
            valueAnimator.addUpdateListener((value) -> {
                float animatedValue = (float) value.getAnimatedValue();
                Log.i("animatedValue", "clickCiv: animatedValue = " + animatedValue);
                photo_viewpager.setScaleX(animatedValue);
                photo_viewpager.setScaleY(animatedValue);

            });
            valueAnimator.setDuration(100);
            valueAnimator.start();
            EventBus.getDefault().post(new ImageScaleEvent(true));
//            AnimationUtil.marginBottomTranslateAnimation(rl_bottom, 0);
//            AnimationUtil.marginTopTranslateAnimation(rl_title, 0);
            return;
        }
        super.onBackPressed();
    }

    private void showDeleteDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setPositiveButton("确定", (view, which) -> {
            File file = new File(mImageEntityBeanList.get(position).path);
            boolean delete = file.delete();
            //删除图片
            ImageEntityBean removedImageBean = mImageEntityBeanList.remove(position);
            Fresco.getImagePipeline().evictFromCache(Uri.parse("file://" + removedImageBean.path));
            photoViewPagerAdapter.notifyDataSetChanged();
            EventBus.getDefault().post(new DeleteImageEvent(removedImageBean));
            //通知系统
            MediaScannerConnection.scanFile(this, new String[]{removedImageBean.path.toString()}, null, null);
//            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + removedImageBean.path)));
        }).setNegativeButton("取消", (view, which) -> {

        }).setTitle("删除图片?");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimary));
        alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
    }
}
