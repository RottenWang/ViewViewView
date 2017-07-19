package com.drwang.views.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 输入法管理
 * <p>
 */
public class InputMethodManagerUtil {

    /**
     * 如果输入法在窗口上已经显示，则隐藏，反之则显示
     *
     * @param context Context
     */
    public static void toggleSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * view为接受软键盘输入的视图，SHOW_FORCED表示强制显示
     *
     * @param context Context
     * @param view    View
     */
    public static void showSoftInput(Activity activity, View view) {
        if (activity != null && !inputIsActive(activity)) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    /**
     * 调用隐藏系统默认的输入法
     *
     * @param activity Activity
     */
    public static void hideSoftInput(Activity activity) {
        if (activity != null && inputIsActive(activity) && activity.getCurrentFocus() != null && activity.getCurrentFocus().getWindowToken() != null) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 获取输入法打开的状态
     *
     * @param context Context
     * @return boolean 若返回true，则表示输入法打开
     */
    public static boolean inputIsActive(Activity activity) {
        return activity != null && ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).isActive();
    }
}
