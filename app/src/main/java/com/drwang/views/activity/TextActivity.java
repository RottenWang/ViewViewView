package com.drwang.views.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;

import com.drwang.views.R;
import com.drwang.views.base.BasicActivity;

import java.io.File;

/**
 * Created by wang on 2018/1/5.
 */

public class TextActivity extends BasicActivity {
    private int REQ_CAMERA = 111;

    @Override
    protected void initializeView() {

    }

    @Override
    protected void initializeData() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{(Manifest.permission.WRITE_EXTERNAL_STORAGE)}, 100);
//            }
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{(Manifest.permission.CAMERA)}, 100);
//            }
//        } else {
//            openCamera();
//
//        }
    }

    private void openCamera() {
        Uri cameraUri;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String imagePaths = Environment.getExternalStorageDirectory() + File.separator + "temp/" + (System.currentTimeMillis() + ".jpg");
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("xxxxxxx", imagePaths).apply();
        File vFile = new File(imagePaths);
        vFile.getParentFile().mkdirs();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, vFile.getAbsolutePath());
            cameraUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        } else {
            cameraUri = Uri.fromFile(vFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        startActivityForResult(intent, REQ_CAMERA);
    }

    @Override
    public int setContentViewRes() {
        return R.layout.activity_text;
    }
}
