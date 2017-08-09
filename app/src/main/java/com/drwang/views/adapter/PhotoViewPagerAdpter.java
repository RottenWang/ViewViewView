package com.drwang.views.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.drwang.views.R;
import com.drwang.views.bean.ImageEntityBean;
import com.drwang.views.support.fresco.FrescoScheme;
import com.drwang.views.support.fresco.FrescoUtils;
import com.drwang.views.view.PhotoViewPager;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.image.ImageInfo;

import java.io.File;
import java.util.List;

import me.relex.photodraweeview.PhotoDraweeView;

import static com.drwang.views.R.id.photo_drawee_view;

/**
 * Created by Administrator on 2017/8/9.
 */

public class PhotoViewPagerAdpter extends PagerAdapter {
    List<ImageEntityBean> mList;
    Activity mActivity;

    public PhotoViewPagerAdpter(Activity activity, List list) {
        mList = list;
        mActivity = activity;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_viewpager, container, false);
        initViewAndData(view, position);
        container.addView(view);
        return view;
    }

    private void initViewAndData(View view, int position) {
        PhotoDraweeView photo_drawee_view = (PhotoDraweeView) view.findViewById(R.id.photo_drawee_view);
        DraweeController controller = FrescoUtils.getController(photo_drawee_view, mList.get(position).width, mList.get(position).height,
                FrescoScheme.SCHEME_FILE + mList.get(position).path, new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        super.onFinalImageSet(id, imageInfo, animatable);
                        if (imageInfo == null || photo_drawee_view == null) {
                            return;
                        }
                        photo_drawee_view.update(imageInfo.getWidth(), imageInfo.getHeight());
                    }
                }
        );
        photo_drawee_view.setController(controller);
        photo_drawee_view.setOnViewTapListener((v, x, y) -> {
            Toast.makeText(mActivity, "haa", Toast.LENGTH_SHORT).show();
        });
        photo_drawee_view.setOnLongClickListener(v -> {
            showDialog(mList.get(position));
            return true;
        });
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
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
