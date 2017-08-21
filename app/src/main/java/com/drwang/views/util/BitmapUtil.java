package com.drwang.views.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.drwang.views.R;

import static android.R.attr.bitmap;
import static android.R.attr.y;

/**
 * Created by Administrator on 2017/8/21.
 */

public class BitmapUtil {

    /**
     * 生成以原图为基准的新的图片
     *
     * @param source 原图
     * @param xSize  宽度的百分比缩减  如 2  则生成原图宽度1/2的宽度的图
     * @param ySize  宽度的百分比缩减 如3  则生成原图1/3的高度的图
     * @return
     */
    public static Bitmap generateReSizeBitmap(Bitmap source, int xSize, int ySize) {
        if (xSize < 1 || ySize < 1 ){
            return null;
        }
        Bitmap.Config c = Bitmap.Config.ARGB_8888;
        Bitmap bitmapEdit = Bitmap.createBitmap(source.getWidth() / xSize, source.getHeight() / ySize, c);
        for (int x = 0; x < source.getWidth(); x += xSize) {
            for (int y = 0; y < source.getHeight(); y += ySize) {
                if (x / xSize >= bitmapEdit.getWidth() | y / ySize >= bitmapEdit.getHeight()) {
                    continue;
                }
                bitmapEdit.setPixel(x / xSize >= bitmapEdit.getWidth() ? bitmapEdit.getWidth() - 1 : x / xSize, y / ySize >= bitmapEdit.getHeight() ? bitmapEdit.getWidth() - 1 : y / ySize,
                        source.getPixel(x, y));
            }
        }
        Log.i("util", "bitmapEdit width = " + bitmapEdit.getWidth() + "height = " + bitmapEdit.getHeight());

        return bitmapEdit;
    }
}
