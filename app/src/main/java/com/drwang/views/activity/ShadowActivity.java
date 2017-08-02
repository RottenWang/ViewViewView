package com.drwang.views.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.drwang.views.R;
import com.drwang.views.adapter.WifiAdapter;
import com.drwang.views.base.BasicActivity;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import butterknife.BindView;

public class ShadowActivity extends BasicActivity {
    @BindView(R.id.tv_wifi)
    TextView tv_wifi;
    @BindView(R.id.recyclerview_wifi)
    RecyclerView recyclerview_wifi;
    List<ScanResult> mList;
    private WifiManager wifiManager;
    LinearLayoutManager layoutManager;
    WifiAdapter mWifiAdapter;

    @Override
    protected void initializeView() {
        if (layoutManager == null) {
            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        }
        recyclerview_wifi.setLayoutManager(layoutManager);
        recyclerview_wifi.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
            } else {
                loadData();
            }
        }

    }

    private void loadData() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        mList = wifiManager.getScanResults();
        clearNoNameWifi(mList);
        sortByLevel(mList);
        mWifiAdapter = new WifiAdapter(this, mList);
        recyclerview_wifi.setAdapter(mWifiAdapter);
        Log.i("a", "loadData: ");
    }

    private void clearNoNameWifi(List<ScanResult> mList) {
        Iterator<ScanResult> iterator = mList.iterator();
        while (iterator.hasNext()) {
            ScanResult next = iterator.next();
            if (next.SSID == null || next.SSID.length() == 0) {
                iterator.remove();
            }
        }
    }

    //将搜索到的wifi根据信号强度从强到弱进行排序
    private void sortByLevel(List<ScanResult> list) {
        for (int i = 0; i < list.size(); i++)
            for (int j = 1; j < list.size(); j++) {
                if (list.get(i).level < list.get(j).level)  //level属性即为强度
                {
                    ScanResult temp = null;
                    temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadData();
            }
        }
    }

    @Override
    protected void initializeData() {

    }

    @Override
    public int setContentViewRes() {
        return R.layout.activity_shadow;
    }
}
