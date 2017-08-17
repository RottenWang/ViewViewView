package com.drwang.views.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.drwang.views.R;
import com.drwang.views.adapter.FilterAdapter;
import com.drwang.views.bean.FilterInfo;
import com.drwang.views.bean.ImageEntityBean;
import com.drwang.views.event.FilterChangeEvent;
import com.drwang.views.event.ImageEvent;
import com.drwang.views.event.RefreshGPUImageEvent;
import com.drwang.views.util.DensityUtil;
import com.drwang.views.util.FileUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImage3x3ConvolutionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImage3x3TextureSamplingFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageAddBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageAlphaBlendFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBilateralFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBoxBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageToneCurveFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageWhiteBalanceFilter;

public class FilterActivity extends AppCompatActivity {
    @BindView(R.id.gl_surface_view)
    GLSurfaceView glsurfaceview;
    @BindView(R.id.rl_slider)
    RelativeLayout rl_slider;
    @BindView(R.id.recycler_view_filter)
    RecyclerView recycler_view_filter;
    @BindView(R.id.seek_bar)
    SeekBar seek_bar;
    private GPUImageFilter gpuImageFilter;
    private float screenHeight;
    private float screenWidth;
    float density;
    int currentPosition;
    int mListSize;
    private List<ImageEntityBean> mList;
    private GPUImage gpuImage;
    private boolean isRenderer;
    private LinearLayoutManager mLayoutManager;
    private List<FilterInfo> mFilterList;
    private FilterAdapter mFilterAdapter;
    private FilterInfo mFilterInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup viewById = (ViewGroup) window.getDecorView().findViewById(android.R.id.content);
            viewById.getChildAt(0).setFitsSystemWindows(true);
        }
        initializeView();
        initializeData();
        initGestureDetector();
        initSeekBar();
    }

    private void initSeekBar() {
        if (mFilterInfo.hasRange) {
            seek_bar.setVisibility(View.VISIBLE);
        } else {
            seek_bar.setVisibility(View.GONE);
        }
        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float progressf = progress / (seek_bar.getMax() * 1.0f);
                if (mFilterInfo != null) {
                    mFilterInfo.setProgress(progressf);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initGestureDetector() {
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

    private void initializeData() {
        ImageEvent stickyEvent = EventBus.getDefault().getStickyEvent(ImageEvent.class);
        EventBus.getDefault().removeStickyEvent(ImageEvent.class);
        density = DensityUtil.getInstance().getDensity(this);
        screenHeight = DensityUtil.getInstance().getScreenHeight(this);
        screenWidth = DensityUtil.getInstance().getScreenWidth(this);
        mFilterInfo = new FilterInfo(new GPUImageFilter(), "custom");
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
    }

    private void initializeView() {
        if (mLayoutManager == null) {
            mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        }
        if (mFilterList == null) {
            mFilterList = new ArrayList<>();
        }
        if (mFilterAdapter == null) {
            mFilterAdapter = new FilterAdapter(this, mFilterList);
            initFilterInfo();
        }
        recycler_view_filter.setLayoutManager(mLayoutManager);
        recycler_view_filter.setAdapter(mFilterAdapter);

    }

    private void initFilterInfo() {
        mFilterList.add(new FilterInfo(new GPUImage3x3ConvolutionFilter(new float[]{1.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 1.0f}), "3x3Convo"));
        mFilterList.add(new FilterInfo(new GPUImageFilter(), "original"));
        mFilterList.add(new FilterInfo(new GPUImageAddBlendFilter(), "addblend"));
        mFilterList.add(new FilterInfo(new GPUImageAlphaBlendFilter(0.8f), "alphaBlend", 0.8f, 0, 1, (p, f) -> {
            ((GPUImageAlphaBlendFilter) f).setMix(p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageBilateralFilter(0.1f), "Bilateral", 0.1f, 0, 10, (p, f) -> {
            ((GPUImageBilateralFilter) f).setDistanceNormalizationFactor(p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageBoxBlurFilter(5f), "BoxBlur", 5f, 0, 10, (p, f) -> {
            ((GPUImageBoxBlurFilter) f).setBlurSize(p);
        }));
        mFilterList.add(new FilterInfo(new GPUImageBrightnessFilter(0.3f), "Brightness", 0.3f, 0, 1, (p, f) -> {
            ((GPUImageBrightnessFilter) f).setBrightness(p);
        }));
    }

    private void resetImageAndFilter() {
        if (currentPosition < 0 || currentPosition > mListSize - 1) {
            currentPosition = currentPosition < 0 ? 0 : currentPosition > mListSize - 1 ? mListSize - 1 : currentPosition;
            return;
        }
        resizeImage(false);
    }

    private void resizeImage(boolean init) {
        Bitmap bitmapOrigin = BitmapFactory.decodeFile(mList.get(currentPosition).path);
        int width = bitmapOrigin.getWidth();
        int height = bitmapOrigin.getHeight();
        int newWidth = width;
        int newHeight = height;
        float scale = 1.0f;
        if (newWidth > screenWidth || newHeight > screenHeight) {
            //格式化宽和高
            if (newWidth > screenWidth) {
                scale = screenWidth / (newWidth * 1.0f);
                newHeight = (int) (newHeight * screenWidth / newWidth);
                newWidth = (int) (screenWidth - 0.5f);
            }
            if (newHeight > screenHeight) {
                scale = scale * screenHeight / (newHeight * 1.0f);
            }
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap bitmap = Bitmap.createBitmap(bitmapOrigin, 0, 0, width, height, matrix, false);
//        Bitmap bitmap = BitmapFactory.decodeFile(mList.get(currentPosition).path, options);
//        Bitmap bitmap = BitmapFactory.decodeFile(mList.get(currentPosition).path, options);
        int finalWidth = (int) (width * scale);
        int finalHeight = (int) (height * scale);
        if (init) {
            gpuImage = new GPUImage(this);
            gpuImage.setGLSurfaceView(glsurfaceview);
            gpuImage.setImage(bitmap);
            gpuImage.setFilter(mFilterInfo.filter);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onFilterChangeEvent(FilterChangeEvent filterChangeEvent) {
        if (gpuImage != null) {
            mFilterInfo = filterChangeEvent.filterInfo;
            gpuImage.setFilter(mFilterInfo.filter);
        }
        if (mFilterInfo.hasRange) {
            seek_bar.setVisibility(View.VISIBLE);
        } else {
            seek_bar.setVisibility(View.GONE);
        }
        seek_bar.setProgress((int) (100 * mFilterInfo.progress));
    }

    @Subscribe
    public void onRefreshGPUImageEvent(RefreshGPUImageEvent event) {
        gpuImage.setFilter(mFilterInfo.filter);
    }

    @OnClick(R.id.tv_save)
    public void save(View v) {
        gpuImage.saveToPictures(FileUtil.getFoldPath(), System.currentTimeMillis() + mList.get(currentPosition).name, new GPUImage.OnPictureSavedListener() {
            @Override
            public void onPictureSaved(Uri uri) {
                Toast.makeText(FilterActivity.this, "URI = " + uri.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
