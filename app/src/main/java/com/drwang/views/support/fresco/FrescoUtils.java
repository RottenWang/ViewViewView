package com.drwang.views.support.fresco;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;

import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;

import static android.R.attr.id;
import static android.R.attr.width;
import static com.drwang.views.R.id.item_iv;

/**
 * Created by Administrator on 2017/8/8.
 */

public class FrescoUtils {

    public static DraweeController getController(SimpleDraweeView simpleDraweeView, int width, int height, String path, ControllerListener<ImageInfo> listener) {
        Uri uri = Uri.parse(path);
        return Fresco.newDraweeControllerBuilder()
                .setOldController(simpleDraweeView.getController()) //复用
                .setAutoPlayAnimations(true) // 支持gif
                .setImageRequest(ImageRequestBuilder.newBuilderWithSource(uri)  //图片请求
                        .setResizeOptions(new ResizeOptions(width, height))  //大小设置
                        .build())
                .setControllerListener(listener).build();
    }

    public static Bitmap returnBitmap(String uri) {
        Uri uriParse = Uri.parse(uri);
        Bitmap bitmap = null;
        FileBinaryResource resource = (FileBinaryResource) Fresco.getImagePipelineFactory().getMainFileCache().getResource(new SimpleCacheKey(uriParse.toString()));
        File file = resource.getFile();
        bitmap = BitmapFactory.decodeFile(file.getPath());
        return bitmap;

    }

}
