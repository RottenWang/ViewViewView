package com.drwang.views.support.fresco;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by Administrator on 2017/8/8.
 */

public class FrescoUtils {
    /**
     * @param simpleDraweeView
     * @param width            生成图片的宽度
     * @param height           生成图片的高度
     * @param path             图片的path   需要加SCHEME   {@link FrescoScheme}
     * @param listener         回调方法监听  可设置为null
     * @return
     */
    public static DraweeController getController(SimpleDraweeView simpleDraweeView, int width, int height, String path, @Nullable ControllerListener<ImageInfo> listener) {
        Uri uri = Uri.parse(path);
        return Fresco.newDraweeControllerBuilder()
                .setOldController(simpleDraweeView.getController()) //复用
                .setAutoPlayAnimations(true) // 支持gif 需要添加依赖compile 'com.facebook.fresco:animated-gif:1.4.0'
                .setImageRequest(ImageRequestBuilder.newBuilderWithSource(uri)  //图片请求
                        .setResizeOptions(new ResizeOptions(width, height))  //大小设置
                        .setLocalThumbnailPreviewsEnabled(true)
                        .build())
                .setControllerListener(listener).build();
    }

}
