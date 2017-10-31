package com.drwang.views.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.drwang.views.R;

/**
 * Created by wang on 2017/10/31.
 */

public class NotificationStyle {


    public static void clickNotify(Context context) {
        Intent clickIntent = new Intent(context, NotificationReceiver.class);
        clickIntent.putExtra(NotificationReceiver.NOTIFY_KEY, "哈哈哈哈");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder notificationCompat = new android.support.v7.app.NotificationCompat.Builder(context)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("哈哈哈哈")
                .setTicker("嘻嘻嘻嘻")
//                .setContent(remoteViews)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent);
        notificationCompat.setFullScreenIntent(null, true);


        int notify = SharedPreferencesUtils.getInt("APP_NOTIFY", 110);
        SharedPreferencesUtils.putInt("APP_NOTIFY", (notify + 1 - 110) % 1000 + 110);
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(notify, notificationCompat.build());

    }

}
