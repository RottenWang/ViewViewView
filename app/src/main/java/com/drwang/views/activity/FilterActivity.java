package com.drwang.views.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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

import com.drwang.views.R;
import com.drwang.views.adapter.FilterAdapter;
import com.drwang.views.bean.FilterInfo;
import com.drwang.views.bean.ImageEntityBean;
import com.drwang.views.event.EditImageEvent;
import com.drwang.views.event.FilterChangeEvent;
import com.drwang.views.event.ImageEvent;
import com.drwang.views.event.ImagePositionChangeEvent;
import com.drwang.views.event.RefreshGPUImageEvent;
import com.drwang.views.support.LocalThreadPoolManager;
import com.drwang.views.support.PriorityRunnable;
import com.drwang.views.util.DensityUtil;
import com.drwang.views.util.FilterUtil;
import com.drwang.views.util.IntentUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.co.cyberagent.android.gpuimage.GPUImage;

import static com.drwang.views.R.id.gl_surface_view;


public class FilterActivity extends AppCompatActivity {
    @BindView(gl_surface_view)
    GLSurfaceView glsurfaceview;
    @BindView(R.id.rl_slider)
    RelativeLayout rl_slider;
    @BindView(R.id.recycler_view_filter)
    RecyclerView recycler_view_filter;
    @BindView(R.id.seek_bar)
    SeekBar seek_bar;
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
    private List<FilterInfo> mFilterList2;

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
        screenHeight = DensityUtil.getInstance().getScreenHeight(this) / 2;
        screenWidth = DensityUtil.getInstance().getScreenWidth(this);
        mFilterInfo = mFilterList.get(0);
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
        if (mFilterList2 == null) {
            mFilterList2 = new ArrayList<>();
        }
        if (mFilterAdapter == null) {
            mFilterAdapter = new FilterAdapter(this, mFilterList);
            initFilterInfo();
        }
        recycler_view_filter.setLayoutManager(mLayoutManager);
        recycler_view_filter.setAdapter(mFilterAdapter);

    }

    private void initFilterInfo() {
        FilterUtil.getFilterInfo(mFilterList);
        FilterUtil.getFilterInfo(mFilterList2);
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
        int finalWidth = (int) (width * scale);
        int finalHeight = (int) (height * scale);
        if (init) {
            gpuImage = new GPUImage(this);
            gpuImage.setGLSurfaceView(glsurfaceview);
            gpuImage.setImage(bitmap);
            gpuImage.setFilter(mFilterInfo.filter);
            glsurfaceview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    glsurfaceview.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    ViewGroup.LayoutParams layoutParams = glsurfaceview.getLayoutParams();
                    layoutParams.width = finalWidth;
                    layoutParams.height = finalHeight;
                    glsurfaceview.setLayoutParams(layoutParams);
                }
            });
        } else {
            gpuImage.deleteImage();
            ViewGroup.LayoutParams layoutParams = glsurfaceview.getLayoutParams();
            layoutParams.width = finalWidth;
            layoutParams.height = finalHeight;
            glsurfaceview.setLayoutParams(layoutParams);
            glsurfaceview.post(() -> {
                gpuImage.setImage(bitmap);
//                gpuImage.requestRender();
            });
        }
        if (init) {
            LocalThreadPoolManager.execute(new PriorityRunnable(10) {
                @Override
                public void run() {
                    Bitmap bitmapOrigin = BitmapFactory.decodeFile(mList.get(currentPosition).path);
                    float scale;
                    float scaleW;
                    float scaleH;
                    scaleW = 50 * density / (bitmapOrigin.getWidth() * 1.0f);
                    scaleH = 50 * density / (bitmapOrigin.getHeight() * 1.0f);
                    scale = scaleW > scaleH ? scaleH : scaleW;
                    Matrix matrix1 = new Matrix();

                    GPUImage gpuThumb = new GPUImage(FilterActivity.this);
                    Bitmap bitmapThumb;
                    try {
                        matrix1.postScale(scale, scale);
                        bitmapThumb = Bitmap.createBitmap(bitmapOrigin, 0, 0, bitmapOrigin.getWidth(), bitmapOrigin.getHeight(), matrix1, false);
                    } catch (IllegalArgumentException e) {
                        matrix1.reset();
                        matrix1.postScale(scaleW, scaleH);
                        bitmapThumb = Bitmap.createBitmap(bitmapOrigin, 0, 0, bitmapOrigin.getWidth(), bitmapOrigin.getHeight(), matrix1, false);
                    }
                    for (int i = 0; i < mFilterList.size(); i++) {
                        FilterInfo filterInfo = mFilterList.get(i);
                        gpuThumb.setFilter(filterInfo.filter);
                        Bitmap bitmapWithFilterApplied = gpuThumb.getBitmapWithFilterApplied(bitmapThumb);
                        filterInfo.bitmap = bitmapWithFilterApplied;
                        int il = i;
                        runOnUiThread(() -> {
                            mFilterAdapter.notifyItemChanged(il);
                        });
                    }
                    runOnUiThread(() -> {
                        gpuImage.setFilter(mFilterInfo.filter);
                    });
                }
            });
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
        GPUImage gpuImageSave = new GPUImage(this);
        int i = mFilterList.indexOf(mFilterInfo);
        gpuImageSave.setFilter(mFilterList2.get(i).filter);
        Bitmap bitmap = Bitmap.createBitmap(gpuImage.mCurrentBitmap);
        Bitmap bitmapWithFilterApplied = gpuImageSave.getBitmapWithFilterApplied(bitmap);
        EventBus.getDefault().postSticky(new EditImageEvent(bitmapWithFilterApplied, mList.get(currentPosition).name));
        IntentUtil.toEditImageActivity(this);
//        gpuImage.saveToPictures(foldPath, name, new GPUImage.OnPictureSavedListener() {
//            @Override
//            public void onPictureSaved(Uri uri) {
//                Toast.makeText(FilterActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
//                String filePath = foldPath + "/" + name;
////                IntentUtil.toEditImageActivity(FilterActivity.this,filePath);
//            }
//        });
    }

    @OnClick(R.id.tv_back)
    public void back(View v) {
        onBackPressed();
    }


    private void checkSeekBar() {
        if (mFilterInfo.hasRange) {
            seek_bar.setVisibility(View.VISIBLE);
        } else {
            seek_bar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(new ImagePositionChangeEvent(currentPosition));
    }
}
