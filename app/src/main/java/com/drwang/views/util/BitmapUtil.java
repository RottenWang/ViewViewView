package com.drwang.views.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

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
     *               这种方式比较耗时
     * @return
     */
    public static Bitmap generateReSizeBitmap(Bitmap source, int xSize, int ySize) {
        if (xSize < 1 || ySize < 1) {
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

    /**
     * 生成以原图为基准的图片大小
     *
     * @param source 原图
     * @param xSize  原图宽度和生成图的比例  比如2 则生成宽度为原图1/2的图片 为负数时 图片会旋转180度
     * @param ySize  原图高度和生成图的比例  比如2 则生成高度为原图1/2的图片
     * @return
     */
    public static Bitmap generateReSizeBitmapUseMatrix(Bitmap source, float xSize, float ySize) {
        if (xSize == 0 || ySize == 0) {
            throw new IllegalArgumentException("xSize or ySize must be not == 0");
        }
        Matrix m = new Matrix();
        m.setScale(1 / (xSize), 1 / (ySize));
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), m, false);
    }

    /**
     * 生成和imageview 同样大小的bitmap
     *
     * @param imageView
     * @param source
     */
    public static void generateSameSizeBitmap(ImageView imageView, Bitmap source) {
        int width = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
        int height = imageView.getHeight() - imageView.getPaddingTop() - imageView.getPaddingBottom();
        if (width <= 0 || height <= 0) {
            imageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int width = imageView.getWidth() - imageView.getPaddingLeft() - imageView.getPaddingRight();
                    int height = imageView.getHeight() - imageView.getPaddingTop() - imageView.getPaddingBottom();
                    generateBitmapUseParticularSize(imageView, source, width, height);
                }
            });
        } else {
            generateBitmapUseParticularSize(imageView, source, width, height);
        }
    }

    /**
     * 生成指定的图片 并设置  规则:  宽度  自适应imageview 的宽  高度 超出 裁剪 少 则自适应
     */
    private static void generateBitmapUseParticularSize(ImageView imageView, Bitmap source, int width, int height) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();
        Matrix matrix = new Matrix();
        float scaleX = width / (sourceWidth * 1.0f);
        float scaleY = height / (sourceHeight * 1.0f);
        matrix.postScale(scaleX, scaleY);
        Bitmap bitmapResize = Bitmap.createBitmap(source, 0, 0, sourceWidth, sourceHeight > height ? height : sourceHeight, matrix, false);
        imageView.setImageBitmap(bitmapResize);
    }


    /**
     * 生成制定宽高的图片
     *
     * @param source
     * @param width
     * @param height
     */
    public static Bitmap generateBitmapUseWidthAndHeight(Bitmap source, int width, int height) {
        Bitmap bitmap;
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();
        Matrix matrix = new Matrix();
        float scaleX = width / (sourceWidth * 1.0f);
        float scaleY = height / (sourceHeight * 1.0f);
        matrix.postScale(scaleX, scaleY);
        bitmap = Bitmap.createBitmap(source, 0, 0, sourceWidth, sourceHeight, matrix, false);
        source.recycle();
        return bitmap;
    }  
    /**
     * 生成宽比高5:4 的小于128kb的图片
     *
     * @param source 原图
     */
    public static Bitmap generateSpecialBitmap(Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();
        //128kb
        long kb_128 = 1024 * 128;
        Bitmap.Config config = source.getConfig();
        long sample;
        //计算图片类型 得出每像素占用空间
        if (config == Bitmap.Config.ARGB_8888) {
            sample = 4;
        } else if (config == Bitmap.Config.ARGB_4444) {
            sample = 2;
        } else if (config == Bitmap.Config.ALPHA_8) {
            sample = 1;
        } else if (config == Bitmap.Config.RGB_565) {
            sample = 2;
        } else {
            sample = 4;
        }
        kb_128 = kb_128 / sample;
        long bitMapSize = width * height * sample;
        if (kb_128 < bitMapSize) {
            //图片大于128kb 需要缩小
            float scale = 1.0f - bitMapSize / (kb_128 * 1.0f);
            //计算需要缩小的scale
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            source = Bitmap.createBitmap(source, 0, 0, width, height, matrix, false);
            width = source.getWidth();
            height = source.getHeight();
        }
        //保持宽高比为5:4 居中截取
        int newWidth;
        int newHeight;
        int deltaWidth;
        int deltaHeight;
        if (width <= height) {
            //宽小于高 高变小 宽不变
            newHeight = width * 4 / 5;
            newWidth = width;
            deltaWidth = 0; //width 变化的距离
            deltaHeight = (height - newHeight) / 2;//y
        } else {
            //宽大于高 让宽为高度的 5 / 4  高度不变
            newWidth = height * 5 / 4;
            newHeight = height;
            deltaWidth = (width - newWidth) / 2;
            deltaHeight = 0;
        }
        Matrix m = new Matrix();
        return Bitmap.createBitmap(source
                , deltaWidth
                , deltaHeight
                , newWidth + deltaWidth > width ? width - deltaWidth : newWidth
                , newHeight + deltaHeight > height ? height - deltaHeight : newHeight
                , m, false);
    }
}
