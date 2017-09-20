package com.drwang.views.util;

/**
 * Created by Administrator on 2017/8/31.
 */

public class MathUtil {
    public static int getAlpha(int lightMutedColor) {
        return (lightMutedColor >> 24) & 0xff;
    }

    public static int getRed(int lightMutedColor) {
        return (lightMutedColor >> 16) & 0xff;
    }

    public static int getGreen(int lightMutedColor) {
        return (lightMutedColor >> 8) & 0xff;
    }

    public static int getBlue(int lightMutedColor) {
        return (lightMutedColor >> 0) & 0xff;
    }
}
