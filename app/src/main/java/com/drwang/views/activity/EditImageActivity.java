package com.drwang.views.activity;

import android.graphics.Bitmap;

import com.drwang.views.R;
import com.drwang.views.base.BasicActivity;
import com.drwang.views.event.EditImageEvent;
import com.drwang.views.view.CanvasView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

public class EditImageActivity extends BasicActivity {
    private Bitmap mBitmap;
    @BindView(R.id.canvas_view)
    CanvasView canvas_view;
    @Override
    protected void initializeView() {
        EditImageEvent stickyEvent = EventBus.getDefault().getStickyEvent(EditImageEvent.class);
        if (stickyEvent != null) {
            mBitmap = stickyEvent.bitmap;
            EventBus.getDefault().removeStickyEvent(stickyEvent);
        }
        canvas_view.setBitmap(mBitmap);
    }

    @Override
    protected void initializeData() {

    }

    @Override
    public int setContentViewRes() {
        return R.layout.activity_edit_image;
    }
}
