package com.drwang.views.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;

import com.drwang.views.R;
import com.drwang.views.base.BasicActivity;
import com.drwang.views.event.EditImageEvent;
import com.drwang.views.util.ShareUtils;
import com.drwang.views.view.CanvasView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;

public class EditImageActivity extends BasicActivity {
    private Bitmap mBitmap;
    @BindView(R.id.canvas_view)
    CanvasView canvas_view;
    @BindView(R.id.cb_save)
    CheckBox cb_save;
    private String name;
    private GPUImageFilter filter;
    private String path;
    private float scale;

    @Override
    protected void initializeView() {
        EditImageEvent imageEvent = EventBus.getDefault().getStickyEvent(EditImageEvent.class);
        if (imageEvent != null) {
            mBitmap = imageEvent.bitmap;
            name = imageEvent.name;
            path = imageEvent.path;
            filter = imageEvent.filter;
            scale = imageEvent.scale;
            EventBus.getDefault().removeStickyEvent(imageEvent);
        }
        canvas_view.setBitmap(mBitmap, name, path, filter, scale);
    }


    @Override
    protected void initializeData() {
        cb_save.setOnCheckedChangeListener((v, b) -> {
            canvas_view.setSaveOriginalImage(b);
        });
    }


    @Override
    public int setContentViewRes() {
        return R.layout.activity_edit_image;
    }


    @OnClick(R.id.btn_save)
    public void saveBitmap(View v) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        } else {
            canvas_view.saveImages(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                canvas_view.saveImages(true);
            }
        }
    }

    @OnClick(R.id.tv_share_to_we_chat_timeline)
    public void shareTimeline(View v) {
        Bitmap currentBitmap = canvas_view.getCurrentBitmap();
        ShareUtils.shareToWeChat(currentBitmap,1);
    }

    @OnClick(R.id.tv_share_to_we_chat)
    public void shareSession(View v) {
        Bitmap currentBitmap = canvas_view.getCurrentBitmap();
        ShareUtils.shareToWeChat(currentBitmap,0);
    }
}
