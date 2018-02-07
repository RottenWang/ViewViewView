package com.drwang.views.adapter;

import android.app.Activity;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.drwang.views.R;
import com.drwang.views.bean.ImageEntityBean;
import com.drwang.views.event.GifChangeEvent;
import com.drwang.views.event.ImageEvent;
import com.drwang.views.support.fresco.FrescoScheme;
import com.drwang.views.support.fresco.FrescoUtils;
import com.drwang.views.util.IntentUtil;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/8.
 */

public class ImageAdapter extends BaseRecyclerViewAdapter<ImageEntityBean> {
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_GIF = 2;
    private ArrayList<ImageEntityBean> mList;
    private int type;
    int width;

    public ImageAdapter(Activity context, ArrayList<ImageEntityBean> list, int type) {
        super(context, list);
        this.mList = list;
        width = context.getResources().getDisplayMetrics().widthPixels / 3;
        this.type = type;
    }

    @Override
    public BaseRecyclerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new ImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false));
    }

    @Override
    protected void onBindHeaderView(View itemView) {

    }

    @Override
    protected void onBindFooterView(View itemView) {

    }

    class ImageHolder extends BaseRecyclerViewHolder {
        private FrameLayout fl_root;
        private SimpleDraweeView item_iv;
        private TextView item_tv;

        public ImageHolder(View itemView) {
            super(itemView);
            item_iv = (SimpleDraweeView) itemView.findViewById(R.id.item_iv);
            fl_root = (FrameLayout) itemView.findViewById(R.id.fl_root);
            item_tv = (TextView) itemView.findViewById(R.id.item_tv);
//            fl_root.getLayoutParams().width = width;
//            fl_root.getLayoutParams().height = width;

        }

        @Override
        public void onBindViewHolder(int position) {
            item_iv.setController(FrescoUtils.getController(item_iv, width, width, FrescoScheme.SCHEME_FILE + mList.get(position).path, new BaseControllerListener<ImageInfo>() {
                        @Override
                        public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                            super.onFinalImageSet(id, imageInfo, animatable);
                        }
                    }
            ));
            itemView.setOnClickListener((v) -> {
                if (type == TYPE_NORMAL){
                    EventBus.getDefault().postSticky(new ImageEvent(mList, position));
                    IntentUtil.toImagePreviewActivity(mActivity);
                }else if (type == TYPE_GIF){
                    EventBus.getDefault().post(new GifChangeEvent(mList.get(position)));
                    mActivity.finish();
                }
            });
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                item_tv.setBackgroundResource(R.drawable.ripple_1);
            }
        }
    }
}
