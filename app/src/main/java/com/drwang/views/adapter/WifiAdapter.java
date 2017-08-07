package com.drwang.views.adapter;

import android.app.Activity;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.drwang.views.R;

import java.util.List;

import static android.net.wifi.WifiConfiguration.Protocol.WPA;

/**
 * Created by Administrator on 2017/7/31.
 */

public class WifiAdapter extends BaseRecyclerViewAdapter<ScanResult> {
    List<ScanResult> mList;
    Activity mActivity;

    public WifiAdapter(Activity context, List<ScanResult> list) {
        super(context, list);
        mList = list;
        mActivity = context;
    }

    @Override
    public BaseRecyclerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        return new WifiViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wifi, parent, false));
    }

    @Override
    protected void onBindHeaderView(View itemView) {

    }

    @Override
    protected void onBindFooterView(View itemView) {

    }

    class WifiViewHolder extends BaseRecyclerViewHolder {

        private final TextView tv_wifi_name;
        private final TextView tv_wifi_encrypt;

        public WifiViewHolder(View itemView) {
            super(itemView);
            tv_wifi_name = (TextView) itemView.findViewById(R.id.tv_wifi_name);
            tv_wifi_encrypt = (TextView) itemView.findViewById(R.id.tv_wifi_encrypt);
        }

        @Override
        public void onBindViewHolder(int position) {
            itemView.setOnTouchListener((v, event) -> {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                itemView.setAlpha(0.7f);
                                break;
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                itemView.setAlpha(1.0f);
                                break;
                        }
                        return false;
                    }
            );
            ScanResult scanResult = mList.get(position);
            String capabilities = scanResult.capabilities;
            String securities = "";
            securities += capabilities.contains("WPA-") ? "WPA" : "";
            securities += capabilities.contains("WPA2-") ? " WPA2" : "";
            securities += capabilities.contains("WPS") ? " WPS" : "";
            securities += capabilities.contains("ESS") ? " ESS" : "";

            tv_wifi_name.setText("WIFI: " + mList.get(position).SSID);
            tv_wifi_encrypt.setText("加密方式: " + securities);
        }
    }
}
