package com.drwang.views.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.drwang.views.R;
import com.drwang.views.base.BasicActivity;
import com.drwang.views.event.EditImageEvent;
import com.drwang.views.view.CanvasView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class EditImageActivity extends BasicActivity {
    private Bitmap mBitmap;
    @BindView(R.id.canvas_view)
    CanvasView canvas_view;
    private String name;

    @Override
    protected void initializeView() {
        EditImageEvent stickyEvent = EventBus.getDefault().getStickyEvent(EditImageEvent.class);
        if (stickyEvent != null) {
            mBitmap = stickyEvent.bitmap;
            name = stickyEvent.name;
            EventBus.getDefault().removeStickyEvent(stickyEvent);
        }
        canvas_view.setBitmap(mBitmap, name);
    }


    @Override
    protected void initializeData() {

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
            canvas_view.saveImages();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                canvas_view.saveImages();
            }
        }
    }
}
