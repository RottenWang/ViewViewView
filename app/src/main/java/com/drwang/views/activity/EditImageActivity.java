package com.drwang.views.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;

import com.drwang.views.R;
import com.drwang.views.base.BasicActivity;
import com.drwang.views.event.EditImageEvent;
import com.drwang.views.view.CanvasView;
import com.drwang.views.view.DragView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class EditImageActivity extends BasicActivity {
    private Bitmap mBitmap;
    @BindView(R.id.canvas_view)
    CanvasView canvas_view;
    @BindView(R.id.dv_left_top)
    DragView dv_left_top;
    @BindView(R.id.dv_right_top)
    DragView dv_right_top;
    @BindView(R.id.dv_left_bottom)
    DragView dv_left_bottom;
    @BindView(R.id.dv_right_bottom)
    DragView dv_right_bottom;
    private String name;
    private float density;
    private int maxTopMargin;
    private int maxWidthMargin;

    @Override
    protected void initializeView() {
        EditImageEvent stickyEvent = EventBus.getDefault().getStickyEvent(EditImageEvent.class);
        if (stickyEvent != null) {
            mBitmap = stickyEvent.bitmap;
            name = stickyEvent.name;
            EventBus.getDefault().removeStickyEvent(stickyEvent);
        }
        density = getResources().getDisplayMetrics().density;
        maxTopMargin = (int) (getResources().getDisplayMetrics().heightPixels - 10 * density + 0.5f);
        maxWidthMargin = (int) (getResources().getDisplayMetrics().widthPixels - 20 * density + 0.5f);
        canvas_view.setBitmap(mBitmap, name);
        canvas_view.setOnSizeChangedInterface((left, top, right, bottom) -> {
            setLocation(left, top, right, bottom);
        });
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



    private void setLocation(float left, float top, float right, float bottom) {
        int leftLocation = (int) (left + 0.5f - density * 10);
        int topLocation = (int) (top + 0.5f - density * 10);
        int rightLocation = (int) (right - density * 10);
        int bottomLocation = (int) (bottom - density * 10);
        RelativeLayout.LayoutParams leftTopLp = (RelativeLayout.LayoutParams) dv_left_top.getLayoutParams();
        leftTopLp.leftMargin = leftLocation < 0 ? 0 : leftLocation;
        leftTopLp.topMargin = topLocation > maxTopMargin ? maxTopMargin : topLocation;
        dv_left_top.setLayoutParams(leftTopLp);
        RelativeLayout.LayoutParams rightTopLp = (RelativeLayout.LayoutParams) dv_right_top.getLayoutParams();
        rightTopLp.leftMargin = rightLocation > maxWidthMargin ? maxWidthMargin : rightLocation;
        rightTopLp.topMargin = topLocation > maxTopMargin ? maxTopMargin : topLocation;
        dv_right_top.setLayoutParams(rightTopLp);
        RelativeLayout.LayoutParams leftBottomLp = (RelativeLayout.LayoutParams) dv_left_bottom.getLayoutParams();
        leftBottomLp.leftMargin = leftLocation < 0 ? 0 : leftLocation;
        leftBottomLp.topMargin = bottomLocation > maxTopMargin ? maxTopMargin : bottomLocation;
        dv_left_bottom.setLayoutParams(leftBottomLp);
        RelativeLayout.LayoutParams rightBottomLp = (RelativeLayout.LayoutParams) dv_right_bottom.getLayoutParams();
        rightBottomLp.leftMargin = rightLocation > maxWidthMargin ? maxWidthMargin : rightLocation;
        rightBottomLp.topMargin = bottomLocation > maxTopMargin ? maxTopMargin : bottomLocation;
        dv_right_bottom.setLayoutParams(rightBottomLp);

    }

}
