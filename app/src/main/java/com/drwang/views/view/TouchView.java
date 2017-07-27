package com.drwang.views.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by Administrator on 2017/7/27.
 */

public class TouchView extends View {

    Paint paint;
    private float density;
    List<PointF> pointFList;
    List<Integer> speedYList;
    List<Integer> speedXList;
    Map<Integer, Integer> overList;
    private Random r;
    private int widthPixels;
    private int heightPixels;
    int count = 100;


    public TouchView(Context context) {
        this(context, null);
    }

    public TouchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        density = getResources().getDisplayMetrics().density;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(20 * density);
        pointFList = new ArrayList<>();
        speedYList = new ArrayList<>();
        speedXList = new ArrayList<>();
        overList = new HashMap();
        r = new Random();
        widthPixels = getResources().getDisplayMetrics().widthPixels;
        heightPixels = getResources().getDisplayMetrics().heightPixels;
        generateViewsInfo();
    }

    private void generateViewsInfo() {
        speedYList.clear();
        speedXList.clear();
        pointFList.clear();
        overList.clear();
        for (int i = 0; i < count; i++) {
            int speed = r.nextInt((int) (density * 10));
            speed = speed < density * 5 ? (int) (density * 5) : speed;
            speedYList.add(speed);
            speed = r.nextInt(2);
            speed = speed == 0 ? r.nextInt((int) density * 3) : -r.nextInt((int) density * 3);
            speedXList.add(speed);
            pointFList.add(new PointF(r.nextInt(widthPixels), r.nextInt(heightPixels / 4)));
        }
        isRestart = false;
    }

    int currentCount = 100;
    boolean isRestart;

    String str2 = new String(Character.toChars(0x261D));
    int overCount;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isRestart) {
            return;
        }
        int sizeBefore = overList.size();
        for (int i = 0; i < count; i++) {
            if (overList.containsKey(i)) {
                continue;
            }
            PointF pointF = pointFList.get(i);
            int speedY = speedYList.get(i);
            int speedX = speedXList.get(i);
            canvas.drawText(str2, pointF.x, pointF.y, paint);
            pointF.y += speedY;
            pointF.x += speedX;
            if (pointF.y > heightPixels || pointF.x < -density || pointF.x > widthPixels) {
                overList.put(i, i);
            }
        }
        overCount += (overList.size() - sizeBefore);
        if (overCount >= count) {
            Toast.makeText(getContext(), "over", Toast.LENGTH_SHORT).show();
            return;
        }
        postInvalidate();
    }

    //重新开始
    public void reStart() {
        isRestart = true;
        generateViewsInfo();
        overCount = 0;
        count = currentCount;
        invalidate();
    }


    public void setCount(int count) {
        currentCount = count;
        this.count = count;
    }

}
