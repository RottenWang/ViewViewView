package com.drwang.views.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.drwang.views.activity.ImageActivity;
import com.drwang.views.activity.MainActivity;

import java.util.List;

/**
 * 通知栏的通知点击监听
 */
public class NotificationReceiver extends BroadcastReceiver {

    public static final String NOTIFY_KEY = "notify_key";

    @Override
    public void onReceive(Context context, Intent intent) {
        String stringExtra = intent.getStringExtra(NOTIFY_KEY);
        Log.i("tag", "onReceive: wangchen" + stringExtra);
        Intent realIntent = new Intent(context, ImageActivity.class);
        realIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        toIntent(context, realIntent);
    }

    public static void toIntent(Context context, Intent realIntent) {
        ActivityManager.RunningTaskInfo runningTaskInfo = isRunningAppProcesses(context);
        if (runningTaskInfo != null) {
            Intent mainIntent = getMainActivityIntent(context);
            context.startActivities(new Intent[]{mainIntent, realIntent});
        } else {
            Intent mainIntent = getMainActivityIntent(context);
            context.startActivities(new Intent[]{mainIntent, realIntent});
        }
    }


    public static Intent getMainActivityIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
        return intent;
    }


    public static void clearNotification(Context context) {
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
    }

    public static ActivityManager.RunningTaskInfo isRunningAppProcesses(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        if (manager != null) {
            List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(220);
            if (runningTaskInfos != null && runningTaskInfos.size() > 0) {
                for (ActivityManager.RunningTaskInfo runningTaskInfo : runningTaskInfos) {
                    if (runningTaskInfo != null) {
                        if (runningTaskInfo.topActivity.getPackageName().contains(context.getPackageName())) {
                            return runningTaskInfo;
                        }
                    }
                }
            }
        }

        return null;
    }


}