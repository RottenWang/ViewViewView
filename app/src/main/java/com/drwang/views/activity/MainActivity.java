package com.drwang.views.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.drwang.views.R;
import com.drwang.views.base.BasicActivity;
import com.drwang.views.kotlin.KotlinBase_1;
import com.drwang.views.util.BitmapUtil;
import com.drwang.views.util.IntentUtil;

import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BasicActivity {

    @BindView(R.id.tv_pieview)
    TextView tv_pieview;
    private KotlinBase_1 kotlinBase_1;
    @BindView(R.id.iv_icon)
    ImageView iv_icon;

    @OnClick({R.id.tv_decoration, R.id.tv_tool_bar, R.id.tv_shadow, R.id.tv_input_soft, R.id.tv_recyclerview, R.id.tv_paint, R.id.tv_inObj, R.id.tv_diedai, R.id.tv_pieview, R.id.tv_printSum, R.id.tv_printFor, R.id.tv_printWhile, R.id.tv_printWhen, R.id.tv_inin, R.id.tv_out, R.id.tv_text})
    public void toOtherActivity(View view) {
        switch (view.getId()) {
            case R.id.tv_pieview:
                IntentUtil.toPieViewActivity(this);
                break;
            case R.id.tv_printSum:
                kotlinBase_1.printInt("a", "b");
                break;
            case R.id.tv_printFor:
                kotlinBase_1.forFor();
                break;
            case R.id.tv_printWhile:
                kotlinBase_1.whileWhile();
                break;
            case R.id.tv_printWhen:
                kotlinBase_1.whenWhen();
                break;
            case R.id.tv_inin:
                kotlinBase_1.inIn();
                break;
            case R.id.tv_out:
                kotlinBase_1.outOut();
                break;
            case R.id.tv_diedai:
                kotlinBase_1.diedai();
                break;
            case R.id.tv_inObj:
                kotlinBase_1.inObj();
                break;
            case R.id.tv_paint:
                IntentUtil.toPaintActivity(this);
                break;
            case R.id.tv_recyclerview:
                IntentUtil.toRecyclerViewActivity(this);
                break;
            case R.id.tv_input_soft:
                IntentUtil.toSoftInputActivity(this);
                break;
            case R.id.tv_shadow:
                IntentUtil.toShadowActivity(this);
                break;
            case R.id.tv_tool_bar:
                IntentUtil.toImageActivity(this);
                break;
            case R.id.tv_decoration:
//                IntentUtil.toDecorationActivity(this);
//                NotificationStyle.clickNotify(this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{(Manifest.permission.WRITE_EXTERNAL_STORAGE)}, 100);
                    } else {
                        Intent intent_pick = new Intent(Intent.ACTION_PICK, null);
                        intent_pick.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent_pick, 10);
                    }
                } else {
                    Intent intent_pick = new Intent(Intent.ACTION_PICK, null);
                    intent_pick.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent_pick, 10);
                }
                break;
            case R.id.tv_text:
                IntentUtil.toTextActivity(this);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent_pick = new Intent(Intent.ACTION_PICK, null);
                intent_pick.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent_pick, 10);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            ContentResolver cr = getContentResolver();
            try {
                Bitmap bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));
                Bitmap bitmap = BitmapUtil.generateBitmapUseWidthAndHeight(bmp, 400, 400);
                iv_icon.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public int setContentViewRes() {
        return R.layout.activity_main;
    }


    @Override
    protected void initializeView() {
//        kotlinBase_1 = new KotlinBase_1();
        Palette palette = null;
        tv_pieview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                tv_pieview.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
//        ButterKnife.bind(this);
//        findViewById(R.id.tv_pieview).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                IntentUtil.toPieViewActivity(MainActivity.this);
//            }
//        });
    }

    @Override
    protected void initializeData() {

    }

}
