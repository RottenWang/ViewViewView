package com.drwang.views.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.drwang.views.R;
import com.drwang.views.adapter.ImageAdapter;
import com.drwang.views.base.BasicActivity;
import com.drwang.views.bean.ImageEntityBean;
import com.drwang.views.event.GifChangeEvent;
import com.drwang.views.event.GifImageInfoEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectTitleImageActivity extends BasicActivity {

    private ArrayList<ImageEntityBean> mGifInfoList;
    @BindView(R.id.rl_gif)
    RecyclerView rl_gif;
    ImageAdapter mGifImageAdapter;
    private GridLayoutManager mGridLayoutManager;

    @Override
    protected void initializeView() {
        GifImageInfoEvent stickyEvent = EventBus.getDefault().getStickyEvent(GifImageInfoEvent.class);
        if (stickyEvent != null) {
            mGifInfoList = stickyEvent.gifInfoList;
        }
        if (mGifImageAdapter == null) {
            mGifImageAdapter = new ImageAdapter(this, mGifInfoList,ImageAdapter.TYPE_GIF);
        }
        if (mGridLayoutManager == null) {
            mGridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        }
        rl_gif.setLayoutManager(mGridLayoutManager);
        rl_gif.setAdapter(mGifImageAdapter);
    }

    @Override
    protected void initializeData() {

    }

    @Override
    public int setContentViewRes() {
        return R.layout.activity_select_title_image;
    }

    @OnClick(R.id.tv_default_gif)
    public void setDefaultGif(View v){
        EventBus.getDefault().post(new GifChangeEvent(null));
        finish();
    }

}
