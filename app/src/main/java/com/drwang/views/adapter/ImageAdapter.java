package com.drwang.views.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.drwang.views.R;
import com.drwang.views.bean.ImageEntityBean;
import com.drwang.views.support.fresco.FrescoScheme;
import com.drwang.views.support.fresco.FrescoUtils;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2017/8/8.
 */

public class ImageAdapter extends BaseRecyclerViewAdapter<ImageEntityBean> {
    private List<ImageEntityBean> mList;
    int width;

    public ImageAdapter(Activity context, List<ImageEntityBean> list) {
        super(context, list);
        this.mList = list;
        float density = context.getResources().getDisplayMetrics().density;
        width = context.getResources().getDisplayMetrics().widthPixels / 2;
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
        private final FrameLayout fl_root;
        private final SimpleDraweeView item_iv;

        public ImageHolder(View itemView) {
            super(itemView);
            item_iv = (SimpleDraweeView) itemView.findViewById(R.id.item_iv);
            fl_root = (FrameLayout) itemView.findViewById(R.id.fl_root);
            fl_root.getLayoutParams().width = width;
            fl_root.getLayoutParams().height = width;
            item_iv.getLayoutParams().width = width;
            item_iv.getLayoutParams().height = width;
        }

        @Override
        public void onBindViewHolder(int position) {
            item_iv.setController(FrescoUtils.getController(item_iv, width, width, FrescoScheme.SCHEME_FILE + mList.get(position).path, new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                    super.onFinalImageSet(id, imageInfo, animatable);

                }
            }));
            itemView.setOnClickListener((v) -> {
                showDialog(mList.get(position));
            });
        }
    }

    private void showDialog(ImageEntityBean imageEntityBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity).setPositiveButton("确定", (view, which) -> {
            File file = new File(imageEntityBean.path);
            boolean delete = file.delete();
            if (delete) {//删除图片
                mList.remove(imageEntityBean);
                notifyDataSetChanged();
                //通知系统
            }
            mActivity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + imageEntityBean.path)));
            Toast.makeText(mActivity, delete + "", Toast.LENGTH_SHORT).show();
        }).setNegativeButton("取消", (view, which) -> {

        }).setTitle("删除图片?");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(mActivity.getResources().getColor(R.color.colorPrimary));
        alertDialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(mActivity.getResources().getColor(R.color.colorAccent));
    }
}
