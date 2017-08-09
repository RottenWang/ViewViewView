package com.drwang.views.util;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017/8/9.
 */

public class AnimationUtil {


    public static void marginBottomTranslateAnimation(View view, float end) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        ValueAnimator va = ValueAnimator.ofFloat(layoutParams.bottomMargin, end);
        va.addUpdateListener((a) -> {
            float animatedValue = (float) a.getAnimatedValue();
            layoutParams.bottomMargin = (int) (animatedValue + 0.5f);
            view.setLayoutParams(layoutParams);
        });
        va.start();
    }

    public static void marginTopTranslateAnimation(View view, float end) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        ValueAnimator va = ValueAnimator.ofFloat(layoutParams.topMargin, end);
        va.addUpdateListener((a) -> {
            float animatedValue = (float) a.getAnimatedValue();
            layoutParams.topMargin = (int) (animatedValue + 0.5f);
            view.setLayoutParams(layoutParams);
        });
        va.start();
    }
}
