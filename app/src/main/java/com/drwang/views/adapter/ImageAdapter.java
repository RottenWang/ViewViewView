package com.drwang.views.adapter;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.drwang.views.R;
import com.drwang.views.bean.ImageEntityBean;
import com.drwang.views.support.fresco.FrescoScheme;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

/**
 * Created by Administrator on 2017/8/8.
 */

public class ImageAdapter extends BaseRecyclerViewAdapter<ImageEntityBean> {
    private List<ImageEntityBean> mList;
    private Activity context;
    int width;

    public ImageAdapter(Activity context, List<ImageEntityBean> list) {
        super(context, list);
        this.mList = list;
        this.context = context;
        float density = context.getResources().getDisplayMetrics().density;
        width = context.getResources().getDisplayMetrics().widthPixels / 2;
    }

    @Override
    public BaseRecyclerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new ImageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false));
    }

    @Override
    protected void onBindHeaderView(View itemView) {

    }

    @Override
    protected void onBindFooterView(View itemView) {

    }

    class ImageHolder extends BaseRecyclerViewHolder {
        private final FrameLayout fl_root;
        private final SimpleDraweeView item_iv;

        public ImageHolder(View itemView) {
            super(itemView);
            item_iv = (SimpleDraweeView) itemView.findViewById(R.id.item_iv);
            fl_root = (FrameLayout) itemView.findViewById(R.id.fl_root);
            fl_root.getLayoutParams().width = width;
            fl_root.getLayoutParams().height = width;
            item_iv.getLayoutParams().width = width;
            item_iv.getLayoutParams().height = width;
        }

        @Override
        public void onBindViewHolder(int position) {
            Uri uri = Uri.parse(FrescoScheme.SCHEME_FILE + mList.get(position).path);
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setResizeOptions(new ResizeOptions(width, width))
                    .build();
            AbstractDraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(item_iv.getController())
                    .setImageRequest(request)
                    .build();
            item_iv.setController(controller);
//            item_iv.setImageURI(FrescoScheme.SCHEME_FILE + mList.get(position).path);
        }
    }
}
