package com.drwang.views.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.drwang.views.R;
import com.drwang.views.bean.ImageEntityBean;
import com.drwang.views.event.ImageEvent;
import com.drwang.views.util.DensityUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageToneCurveFilter;

public class FilterActivity extends AppCompatActivity {
    @BindView(R.id.gl_surface_view)
    GLSurfaceView glsurfaceview;
    @BindView(R.id.rl_slider)
    RelativeLayout rl_slider;
    private GPUImageFilter gpuImageFilter;
    private float screenHeight;
    private float screenWidth;
    float density;
    int currentPosition;
    int mListSize;
    private List<ImageEntityBean> mList;
    private GPUImage gpuImage;
    private boolean isRenderer;
    private GPUImageFilter filter;

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
        filter = new GPUImageToneCurveFilter();
        density = DensityUtil.getInstance().getDensity(this);
        screenHeight = DensityUtil.getInstance().getScreenHeight(this);
        screenWidth = DensityUtil.getInstance().getScreenWidth(this);
        gpuImageFilter = new GPUImageFilter();
        currentPosition = stickyEvent.position;
        mList = stickyEvent.mList;
        mListSize = stickyEvent.mList.size();
        resizeImage(true);
        rl_slider.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || MotionEvent.ACTION_MOVE == event.getAction()) {
                    mGestureDetector.onTouchEvent(event);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    isRenderer = false;
                }
                return true;
            }
        });

        mGestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                Log.i("mGestureDetector", "mGestureDetector: onDown");
                deltaX = 0;
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                Log.i("mGestureDetector", "mGestureDetector: onShowPress");
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.i("mGestureDetector", "mGestureDetector: onSingleTapUp");
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                deltaX += distanceX;
                if (Math.abs(deltaX) > density * 40 && !isRenderer) {
                    isRenderer = true;
                    if (deltaX > 40) {
                        currentPosition++;
                    } else {
                        currentPosition--;
                    }
                    resetImageAndFilter();
                    deltaX = 0;
                }
                Log.i("mGestureDetector", "mGestureDetector: onScroll" + "distanceX = " + distanceX + ",distanceY = " + distanceY);
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.i("mGestureDetector", "mGestureDetector: onLongPresss");

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                Log.i("mGestureDetector", "mGestureDetector: onFling" + "velocityX = " + velocityX + ",velocityY = " + velocityY);

                return false;
            }
        });
    }

    private void resetImageAndFilter() {
        Log.i("resetImageAndFilter", "resetImageAndFilter: currentPosition = " + currentPosition);
        if (currentPosition < 0 || currentPosition > mListSize - 1) {
            currentPosition = currentPosition < 0 ? 0 : currentPosition > mListSize - 1 ? mListSize - 1 : currentPosition;
            return;
        }
        resizeImage(false);
    }

    private void resizeImage(boolean init) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mList.get(currentPosition).path, options);
        int width = options.outWidth;
        int height = options.outHeight;
        int newWidth = width;
        int newHeight = height;
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
        Bitmap bitmap = BitmapFactory.decodeFile(mList.get(currentPosition).path, options);
        int finalWidth = width / options.inSampleSize;
        int finalHeight = height / options.inSampleSize;
        if (init) {
            gpuImage = new GPUImage(this);
            gpuImage.setGLSurfaceView(glsurfaceview);
            gpuImage.setImage(bitmap);
            gpuImage.setFilter(gpuImageFilter);
        }
        if (init) {
            glsurfaceview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    glsurfaceview.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    ViewGroup.LayoutParams layoutParams = glsurfaceview.getLayoutParams();
                    layoutParams.width = finalWidth;
                    layoutParams.height = finalHeight;
                    glsurfaceview.setLayoutParams(layoutParams);
                    gpuImage.setFilter(filter);
//                gpuImage.requestRender();
                }
            });
        } else {
            gpuImage.deleteImage();
            ViewGroup.LayoutParams layoutParams = glsurfaceview.getLayoutParams();
            layoutParams.width = finalWidth;
            layoutParams.height = finalHeight;
            glsurfaceview.setLayoutParams(layoutParams);
            gpuImage.setImage(bitmap);
        }

    }

    float deltaX;

    GestureDetector mGestureDetector;
}
