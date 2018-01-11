package com.drwang.views.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Drawable 着色的后向兼容方案
 * <p>
 * Created by luoshiqiang on 15/9/16.
 */
public class TintColorUtil {

    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    public static Drawable tintDrawable(Context context, Drawable drawable, int colorResID) {
        return tintDrawable(
                drawable.mutate(),
                ColorStateList.valueOf(context.getResources().getColor(colorResID))
        );
    }

    public static void tintDrawableClear(Context context, ImageView imageView, int colorResID) {
        Drawable originalDrawable = imageView.getDrawable();
        if (originalDrawable == null) {
            return;
        }
        imageView.setImageDrawable(
                tintDrawable(
                        originalDrawable.mutate(),
                        ColorStateList.valueOf(context.getResources().getColor(colorResID))
                )
        );
    }

    public static void tintDrawable(Context context, ImageView imageView, int colorResID) {
        Drawable originalDrawable = imageView.getDrawable();
        if (originalDrawable == null) {
            return;
        }
        imageView.setImageDrawable(
                tintDrawable(
                        originalDrawable.mutate(),
                        ColorStateList.valueOf(context.getResources().getColor(colorResID))
                )
        );
    }

    public static void tintDrawable(Context context, Button button, int colorResID) {
        Drawable originalDrawable = button.getCompoundDrawables()[0];
        if (originalDrawable == null) {
            return;
        }
        Drawable drawable = tintDrawable(
                originalDrawable.mutate(),
                ColorStateList.valueOf(context.getResources().getColor(colorResID))
        );
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        button.setCompoundDrawables(
                drawable, null, null, null
        );
    }

    public static void tintDrawable(Context context, EditText editText, int colorResID) {
        Drawable originalDrawable = editText.getCompoundDrawables()[0];
        if (originalDrawable == null) {
            return;
        }
        Drawable drawable = tintDrawable(
                originalDrawable.mutate(),
                ColorStateList.valueOf(context.getResources().getColor(colorResID))
        );
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        editText.setCompoundDrawables(
                drawable, null, null, null
        );
    }

    public static void tintDrawable(Context context, TextView textView, int colorResID) {
        Drawable originalDrawable = textView.getCompoundDrawables()[0];
        if (originalDrawable == null) {
            return;
        }
        Drawable drawable = tintDrawable(
                originalDrawable.mutate(),
                ColorStateList.valueOf(context.getResources().getColor(colorResID))
        );
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(
                drawable, null, null, null
        );
    }

    public static void tintDrawable(ImageView imageView, int color) {
        Drawable originalDrawable = imageView.getDrawable();
        if (originalDrawable == null) {
            return;
        }
        imageView.setImageDrawable(
                tintDrawable(
                        originalDrawable.mutate(),
                        ColorStateList.valueOf(color)
                )
        );
    }

    public static void tintDrawable(SimpleDraweeView imageView, Drawable originalDrawable, int color) {
        if (originalDrawable == null) {
            return;
        }
        imageView.getHierarchy().setPlaceholderImage(
                tintDrawable(
                        originalDrawable.mutate(),
                        ColorStateList.valueOf(color)
                )
        );
    }
}
