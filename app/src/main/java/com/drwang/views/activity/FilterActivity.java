package com.drwang.views.activity;

import android.app.ActionBar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import com.drwang.views.R;
import com.drwang.views.event.ImageEvent;
import com.drwang.views.util.DensityUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageColorInvertFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

import static android.R.attr.bitmap;

public class FilterActivity extends AppCompatActivity {
    @BindView(R.id.gl_surface_view)
    GLSurfaceView glsurfaceview;

    private GPUImageFilter gpuImageFilter;
    private float screenHeight;
    private float screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        ImageEvent stickyEvent = EventBus.getDefault().getStickyEvent(ImageEvent.class);
        EventBus.getDefault().removeStickyEvent(ImageEvent.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup viewById = (ViewGroup) window.getDecorView().findViewById(android.R.id.content);
            viewById.getChildAt(0).setFitsSystemWindows(true);
        }
        screenHeight = DensityUtil.getInstance().getScreenHeight(this);
        screenWidth = DensityUtil.getInstance().getScreenWidth(this);
        gpuImageFilter = new GPUImageFilter();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(stickyEvent.mList.get(stickyEvent.position).path, options);
        int width = options.outWidth;
        int height = options.outHeight;
        int newWidth = width;
        int newHeight = height;
        Log.i("onCreate", "onCreate: original width = " + width + ",height = " + height);
        if (newWidth > screenWidth || newHeight > screenHeight) {
            //格式化宽和高
            if (newWidth > screenWidth) {
                newHeight = (int) (newHeight * screenWidth / newWidth);
                newWidth = (int) (screenWidth - 0.5f);
            }
            if (newHeight > screenHeight) {
                newWidth = (int) (newWidth * screenHeight / newHeight);
                newHeight = (int) (screenHeight - 0.5f);
            }
        }
        options.inSampleSize = width / newWidth;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(stickyEvent.mList.get(stickyEvent.position).path, options);
        int finalWidth = width / options.inSampleSize;
        int finalHeight = height / options.inSampleSize;
        Log.i("onCreate", "onCreate: final width = " + finalWidth + ",height = " + finalHeight);
        Log.i("onCreate", "onCreate: final bitmap width = " + bitmap.getWidth() + ",height = " + bitmap.getHeight());
        GPUImage gpuImage = new GPUImage(this);
        gpuImage.setGLSurfaceView(glsurfaceview);
        gpuImage.setImage(bitmap);
        gpuImage.setFilter(gpuImageFilter);
        glsurfaceview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                glsurfaceview.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ViewGroup.LayoutParams layoutParams = glsurfaceview.getLayoutParams();
                layoutParams.width = finalWidth;
                layoutParams.height = finalHeight;
                glsurfaceview.setLayoutParams(layoutParams);
                glsurfaceview.post(() -> {
                    Log.i("onCreate", "onCreate: glsurfaceview width = " + glsurfaceview.getLayoutParams().width + ",height = " + glsurfaceview.getLayoutParams().height);
                });
                gpuImage.requestRender();
            }
        });


//        gpuImage.saveToPictures("GPUImage", "ImageFilter.jpg", null);
    }
}
