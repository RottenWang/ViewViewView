package com.drwang.views.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.drwang.views.R;

import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */

public class ExampleAdapter extends BaseRecyclerViewAdapter<String> {
    List<String> stringList;
    Activity context;

    public ExampleAdapter(Activity context, List<String> list) {
        super(context, list);
        stringList = list;
        this.context = context;
    }

    @Override
    public BaseRecyclerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new ExampleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_text, parent, false));
    }


    @Override
    protected void onBindHeaderView(View itemView) {

    }

    @Override
    protected void onBindFooterView(View itemView) {

    }

    class ExampleViewHolder extends BaseRecyclerViewHolder {
        TextView tv_test;

        public ExampleViewHolder(View itemView) {
            super(itemView);
            tv_test = (TextView) itemView.findViewById(R.id.tv_test);
        }

        @Override
        public void onBindViewHolder(int position) {
            tv_test.setText(stringList.get(position));
        }
    }
}
