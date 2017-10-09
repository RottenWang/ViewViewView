package com.drwang.views.activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.drwang.views.R;

import java.util.Set;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
            overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
        }, 2000);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{(Manifest.permission.WRITE_EXTERNAL_STORAGE)}, 100);
//            } else {
//                getSupportLoaderManager().initLoader(0, null, imageLoader);
//            }
//        } else {
//            getSupportLoaderManager().initLoader(0, null, imageLoader);
//
//        }
        /*------------蓝牙相关 部分代码-------------*/
//        initBlueTooth();
        /*------------------------*/
    }

    private void initBlueTooth() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            } else {
                startBlueTooth();
            }
        } else {
            startBlueTooth();
        }
    }

    //开始扫描蓝牙
    private void startBlueTooth() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 102);
        } else {
            startSearchBlueTooth();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i(TAG, "打开蓝牙成功");
                startSearchBlueTooth();
            } else {
                Log.i(TAG, "打开蓝牙失败");
            }
        }
    }

    private void startSearchBlueTooth() {
        //获取本机蓝牙名称
        String name = mBluetoothAdapter.getName();
        //获取本机蓝牙地址
        String address = mBluetoothAdapter.getAddress();
        Log.d(TAG, "本机蓝牙 名称:" + name + ",地址" + address);
        //获取已配对蓝牙设备
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice bonddevice : devices) {
            Log.d(TAG, "已配对蓝牙  名称:" + bonddevice.getName() + ",地址:" + bonddevice.getAddress());
        }
        //开始搜索蓝牙设备
        mBluetoothAdapter.startDiscovery();
        registerBlueToothReceiver();
    }

    private void registerBlueToothReceiver() {
        IntentFilter filter = new IntentFilter();
//发现设备
        filter.addAction(BluetoothDevice.ACTION_FOUND);
//设备连接状态改变
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//蓝牙设备状态改变
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mBluetoothReceiver, filter);
    }

    private BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "mBluetoothReceiver action =" + action);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {//每扫描到一个设备，系统都会发送此广播。
                //获取蓝牙设备
                BluetoothDevice scanDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (scanDevice == null || scanDevice.getName() == null) return;
                Log.d(TAG, "扫描到的设备 名称:" + scanDevice.getName() + ",地址:" + scanDevice.getAddress());
                //蓝牙设备名称
                String name = scanDevice.getName();
//                if (name != null && name.equals(BLUETOOTH_NAME)) {
//                    mBluetoothAdapter.cancelDiscovery();
//                    //取消扫描
////                    mProgressDialog.setTitle(getResources().getString(R.string.progress_connecting));                   //连接到设备。
//                    mBlthChatUtil.connect(scanDevice);
                //连接蓝牙  需要在子线程去连接
//                BluetoothSocket socket = null;
//                try {
//                    socket = scanDevice.createRfcommSocketToServiceRecord(UUID.randomUUID());
//                    socket.connect();
//                } catch (IOException e) {
////                连接后 会阻塞线程  成功则连接完成  失败则走异常
//                    if (socket != null) {
//                        try {//关闭socket
//                            socket.close();
//                        } catch (IOException e1) {
//                            //ignored
//                        }
//                    }
//                }
//                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.d(TAG, "扫描完成");
            }
        }

    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getSupportLoaderManager().initLoader(0, null, imageLoader);
            }
        }
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                startBlueTooth();
            }
        }
    }

    LoaderManager.LoaderCallbacks<Cursor> imageLoader = new LoaderManager.LoaderCallbacks<Cursor>() {

        private String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.WIDTH,
                MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media._ID
        };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            CursorLoader cursorLoader = new CursorLoader(getApplicationContext(),
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION, null, null, IMAGE_PROJECTION[2] + " DESC"
            );
            return cursorLoader;
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            if (data != null) {
                if (data.moveToFirst()) {
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        int width = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[3]));
                        int height = data.getInt(data.getColumnIndexOrThrow(IMAGE_PROJECTION[4]));
                        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(name)) {
                            continue;
                        }
                        if (width <= 0 || height <= 0) {
                            continue;
                        }


                    } while (data.moveToNext());
                }


            }

        }

    };

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        try {
//            unregisterReceiver(mBluetoothReceiver);
//        } catch (Exception e) {
//
//        }
//    }
}