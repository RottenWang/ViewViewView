package com.drwang.views.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {
    private static final int TYPE_HEAD = 60001;
    private static final int TYPE_FOOT = 60002;
    private static final int TYPE_NORMAL = 333;
    public List<T> mList;
    public Activity mActivity;
    private View headerView;
    private View footerView;
    private boolean hasHeader;
    private boolean hasFooter;

    public BaseRecyclerViewAdapter(Activity context, List<T> list) {
        mActivity = context;
        mList = list;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD) {
            return new HeadViewHolder(headerView);
        } else if (viewType == TYPE_FOOT) {
            return new FooterViewHolder(footerView);
        } else if (viewType == TYPE_NORMAL) {
            return onCreateHolder(parent, viewType);
        }
        return null;
    }

    public abstract BaseRecyclerViewHolder onCreateHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEAD) {
            onBindHeaderView(holder.itemView);
        } else if (getItemViewType(position) == TYPE_FOOT) {
            onBindFooterView(holder.itemView);
        } else if (getItemViewType(position) == TYPE_NORMAL) {
            onBindItemView(holder, getRealPosition(position));
        }
    }

    protected void onBindItemView(BaseRecyclerViewHolder holder, int position) {
        holder.onBindViewHolder(position);
    }

    ;


    @Override
    public int getItemViewType(int position) {

        if (hasHeader() && position == 0) {
            return TYPE_HEAD;
        } else if (hasFooter() && position == getItemCount() - 1) {
            return TYPE_FOOT;
        }
        return TYPE_NORMAL;
    }

    protected boolean hasFooter() {
        return hasFooter;
    }

    ;

    public int getRealPosition(int position) {
        if (hasHeader()) {
            return position - 1;
        } else {
            return position;
        }
    }

    protected T getItemByPosition(int position) {
        return mList.get(getRealPosition(position));
    }

    protected abstract void onBindHeaderView(View itemView);

    protected abstract void onBindFooterView(View itemView);

    @Override
    public int getItemCount() {
        int count = 0;
        count += hasHeader ? 1 : 0;
        count += hasFooter ? 1 : 0;
        count += mList == null ? 0 : mList.size();
        Log.d("a", "getItemCount: " + count);
        return count;
    }

    public boolean hasHeader() {
        return hasHeader;
    }

    //添加header
    public void setHeaderView(View headerView) {
        hasHeader = true;
        this.headerView = headerView;
    }


    //移除header
    public void removeHeaderView() {
        hasHeader = false;
        this.headerView = null;
    }  //移除header

    //添加footer
    public void setFooterView(View footerView) {
        hasFooter = true;
        this.footerView = footerView;
    }

    public void removeFooterView() {
        hasFooter = false;
        this.footerView = null;
    }


    public class HeadViewHolder extends BaseRecyclerViewHolder {

        public HeadViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
        }
    }

    public class FooterViewHolder extends BaseRecyclerViewHolder {

        public FooterViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
        }
    }

}
