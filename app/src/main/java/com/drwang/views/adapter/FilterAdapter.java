package com.drwang.views.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.drwang.views.R;
import com.drwang.views.bean.FilterInfo;
import com.drwang.views.event.FilterChangeEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Administrator on 2017/8/17.
 */

public class FilterAdapter extends BaseRecyclerViewAdapter<FilterInfo> {

    private int currentPosition;
    private int lastPosition;

    public FilterAdapter(Activity context, List<FilterInfo> list) {
        super(context, list);
    }

    @Override
    public BaseRecyclerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new FilterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter, parent, false));
    }

    @Override
    protected void onBindHeaderView(View itemView) {

    }

    @Override
    protected void onBindFooterView(View itemView) {

    }

    class FilterViewHolder extends BaseRecyclerViewHolder {

        private TextView tv_filter_name;
        private ImageView iv_filter_preview;
        private ImageView iv_foreground;


        public FilterViewHolder(View itemView) {
            super(itemView);
            tv_filter_name = (TextView) itemView.findViewById(R.id.tv_filter_name);
            iv_filter_preview = (ImageView) itemView.findViewById(R.id.iv_filter_preview);
            iv_foreground = (ImageView) itemView.findViewById(R.id.iv_foreground);
        }

        @Override
        public void onBindViewHolder(int position) {
            tv_filter_name.setText(mList.get(position).filterName);
            itemView.setOnClickListener((v) -> {
                EventBus.getDefault().post(new FilterChangeEvent(mList.get(position)));
                currentPosition = position;
                notifyDataSetChanged();
            });
            if (currentPosition == position) {
                iv_foreground.setVisibility(View.VISIBLE);
            } else {
                iv_foreground.setVisibility(View.INVISIBLE);
            }
            iv_filter_preview.setImageBitmap(mList.get(position).bitmap);
        }
    }
}
