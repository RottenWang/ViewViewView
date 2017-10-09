package com.drwang.views.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.text.DecimalFormat;

/**
 * Created by wang on 2017/10/9.
 * 通过重绘来平移
 */

public class SelectedMoneyView2 extends View {

    private Paint paint;
    private float density;
    private float deltaX; //每个刻度之间的距离
    private float scrollerXDefault;
    private float scrollerCurrent;
    private float totalWidth;
    private int w;
    private int range;
    private OnSelectedChangedListener mOnSelectedChangedListener;
    private int rangeMin;
    private int rangeMax;
    private int minMoney;
    private int maxMoney;
    private int devideMoneyColor;
    private int moneyColor;
    private int middleColor;
    private int deltaMoney;
    private boolean isFullScreenWidth;  //是否需要满屏幕
    int currentMoney;
    DecimalFormat df = new DecimalFormat("0.00");
    private int titleColor;

    public SelectedMoneyView2(Context context) {
        this(context, null);
    }

    public SelectedMoneyView2(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectedMoneyView2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        density = getResources().getDisplayMetrics().density;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(density);
        deltaX = 15 * density;
        setMoney(100, 600, 3600, 3800, true);
        setColors(Color.GRAY, Color.RED, Color.RED, Color.GRAY);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        calculateTranslate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isNeedResetScrollerDefault) {
            isNeedResetScrollerDefault = false;
            scrollerXDefault += deltaX / 2.0f;
            scrollerCurrent = scrollerXDefault;
        }
        paint.setTextSize(density * 16);
        paint.setColor(devideMoneyColor);
        paint.setStyle(Paint.Style.FILL);
        int height = getHeight();
        float lineShort = density * 10; //短刻度的长度
        float lineLong = density * 20;  //长刻度的高度
        float startX = 0;
        float startY;
        //画金额的线
        canvas.save();
        canvas.translate(scrollerCurrent, 0);
        for (int i = 0; i <= range; i++) {
            if (i % 5 == 0) {
                startY = height - lineLong;
                drawText(i, canvas, startX, startY - 10 * density);
            } else {
                startY = height - lineShort;
            }

            canvas.drawLine(startX, startY, startX, height, paint);
            startX += deltaX;
        }

        canvas.restore();
    }

    @Override
    public void onDrawForeground(Canvas canvas) { //画其他部分
        super.onDrawForeground(canvas);
        int width = getWidth();
        int height = getHeight();
        float lineLength = height - 50 * density;
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(middleColor);
        //画中线
        canvas.drawLine(width / 2, lineLength, width / 2, height, paint);
        //画小圆
        canvas.drawCircle(width / 2, lineLength, 2 * density, paint);
        //画金额
        paint.setStyle(Paint.Style.FILL);
        drawMoney(canvas, width / 2, height / 2); //金额的位置
        //画标题
        drawTitle(canvas, width / 2, (int) (height / 2 - 30 * density));
    }

    private void drawTitle(Canvas canvas, int centerX, int centerY) {
        paint.setColor(titleColor);
        paint.setTextSize(17 * density);
        String text = "借款金额(元)";
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect); //获取文字边界
        float lineX = centerX - (rect.left + rect.right) / 2.0f; // 计算文字的基准线
        float lineY = centerY - (rect.top + rect.bottom) / 2.0f;
        canvas.drawText(text, lineX, lineY, paint);

    }


    private void drawMoney(Canvas canvas, int centerX, int centerY) {
        paint.setTextSize(25 * density);
        paint.setColor(moneyColor);
        float v = scrollerCurrent - scrollerXDefault;
        int delta;
        if (v >= 0) {
            delta = (int) (v / deltaX + 0.5f);
        } else {
            delta = (int) (v / deltaX - 0.5f);
        }
//        if (currentX == delta || delta > rangeMax - minMoney || delta < rangeMin + (range - maxMoney)) { //如果本次计算的位置相同 或者超出了默认的范围  那么就return
//            return delta * deltaX + scrollerXDefault;
//        }
        int money = (range / 2 - delta) * deltaMoney;
        if (money < minMoney * deltaMoney) {
            money = minMoney * deltaMoney;
        }
        if (money > maxMoney * deltaMoney) {
            money = maxMoney * deltaMoney;
        }
        if (currentMoney != money) {
            Log.i("wang", "drawMoney: money = " + money);
            if (mOnSelectedChangedListener != null) {
                mOnSelectedChangedListener.onChanged(money);
            }
        }
        currentMoney = money;
        Rect rect = new Rect();
        String text = df.format(money);
        paint.getTextBounds(text, 0, text.length(), rect); //获取文字边界
        float lineX = centerX - (rect.left + rect.right) / 2.0f; // 计算文字的基准线
        float lineY = centerY - (rect.top + rect.bottom) / 2.0f;
        canvas.drawText(text, lineX, lineY, paint);

    }

    private void drawText(int i, Canvas canvas, float x, float y) {
        String text = String.valueOf(i * deltaMoney);
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect); //获取文字边界
        float lineX = x - (rect.left + rect.right) / 2.0f; // 计算文字的基准线
        float lineY = y - (rect.top + rect.bottom) / 2.0f;
        //画文字
        canvas.drawText(text, lineX, lineY, paint);
    }

    float startX;
    float moveX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                startX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startX == 0) {
                    startX = event.getX();
                    return true;
                }
                moveX = event.getX();
                scrollerCurrent += moveX - startX;
                startX = moveX;
                calculatorCurrentMoney();
                invalidate();//申请重绘
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                startX = 0;
                /*回到最近的刻度*/
                if (scrollerCurrent > w / 2 - minMoney * deltaX) { //向右滑 超过最小范围
                    //超过左边
                    animToLocation(scrollerCurrent, w / 2 - minMoney * deltaX, 200);
//                    scrollTo(scrollerXDefault, 0);
                } else if (scrollerCurrent < -(totalWidth - w / 2 - (range - maxMoney) * deltaX)) { //向左滑 超过中线
                    animToLocation(scrollerCurrent, -(totalWidth - w / 2 - (range - maxMoney) * deltaX), 200);
                } else {  //在中间  需要计算 归零点
                    float lineX = calculatorCurrentMoney();
                    animToLocation(scrollerCurrent, lineX, 200);
                }
                break;
        }
        return true;
    }

    int currentX = -1;

    private float calculatorCurrentMoney() {
        float v = scrollerCurrent - scrollerXDefault;
        int delta;
        if (v >= 0) {
            delta = (int) (v / deltaX + 0.5f);
        } else {
            delta = (int) (v / deltaX - 0.5f);
        }
//        if (currentX == delta || delta > rangeMax - minMoney || delta < rangeMin + (range - maxMoney)) { //如果本次计算的位置相同 或者超出了默认的范围  那么就return
//            return delta * deltaX + scrollerXDefault;
//        }
        currentX = delta;
        int money = (range / 2 - delta) * deltaMoney;
        return delta * deltaX + scrollerXDefault;
    }

    private void animToLocation(float start, float end, long duration) {
        ObjectAnimator animateX = ObjectAnimator.ofFloat(this, "scrollerCurrent", start, end);
        animateX.setDuration(duration);
        animateX.start();
    }


    public void setScrollerCurrent(float scrollerCurrent) {
        this.scrollerCurrent = scrollerCurrent;
        invalidate();
    }

    boolean isNeedResetScrollerDefault;

    /**
     * 设置view显示的金额最大值
     *
     * @param range
     */
    public void setRange(int range) {
        range = range / deltaMoney;
        if (range % 5 != 0) {
            range += 5 - range % 5;
        }
        if (range % 2 != 0) {
            isNeedResetScrollerDefault = true;
        }
        this.range = range;
        rangeMin = -range / 2;
        rangeMax = range / 2;
    }

    /**
     * 设置金额可选最小值
     *
     * @param minMoney
     */

    public void setMinMoney(int minMoney) {

        this.minMoney = minMoney / deltaMoney;
    }

    /**
     * 设置金额可选最大值
     *
     * @param maxMoney
     */
    public void setMaxMoney(int maxMoney) {
        this.maxMoney = maxMoney / deltaMoney;
    }

    /**
     * 金额变化回调的监听
     *
     * @param onSelectedChangedListener
     */
    public void setOnSelectedChangedListener(OnSelectedChangedListener onSelectedChangedListener) {
        this.mOnSelectedChangedListener = onSelectedChangedListener;
    }

    public interface OnSelectedChangedListener {
        void onChanged(int money);
    }

    /**
     * 设置下方金额文字和刻度尺的颜色
     *
     * @param color
     */
    private void setDeviDeMoneyColor(int color) {
        this.devideMoneyColor = color;
    }

    /**
     * 设置借款金额下方的金额的颜色
     *
     * @param color
     */
    private void setMoneyColor(int color) {
        this.moneyColor = color;
    }

    /**
     * 设置中线的颜色
     *
     * @param color
     */
    private void setMiddleLineColor(int color) {
        this.middleColor = color;
    }

    private void setTitleColor(int color) {
        this.titleColor = color;
    }

    /**
     * 设置刻度money
     *
     * @param money
     */
    private void setDeltaMoney(int money) {
//        if (money % 100 != 0) {
//            money = 100 * (money / 100);
//        }
        deltaMoney = money;
    }

    /**
     * 设置各种金额  需要同时设置
     *
     * @param deltaMoney 刻度金额
     * @param minMoney   //最小的选择金额
     * @param maxMoney   //最大的选择金额
     * @param ranges     //显示的范围  从 0 到range
     */
    public void setMoney(int deltaMoney, int minMoney, int maxMoney, int ranges) {
        setMoney(deltaMoney, minMoney, maxMoney, ranges, false);
    }

    private void setMoney(int deltaMoney, int minMoney, int maxMoney, int ranges, boolean isInit) {

        setDeltaMoney(deltaMoney);
        setMinMoney(minMoney);
        setMaxMoney(maxMoney);
        setRange(ranges);
        if (!isInit) {
            calculateTranslate();

        }
        requestLayout();
        invalidate();

    }

    private void calculateTranslate() {
        totalWidth = deltaX * range;
        if (totalWidth <= w && isFullScreenWidth) {
            totalWidth = w;
            deltaX = totalWidth / (range * 1.0f);
            scrollerXDefault = 0;
        } else {
            scrollerXDefault = -(totalWidth - w) / 2.0f;
        }
        scrollerCurrent = scrollerXDefault;
    }

    /**
     * 设置是否刻度必须满屏幕
     * 默认不满
     *
     * @param isFullScreenWidth
     */
    public void setIsFullScreenWidth(boolean isFullScreenWidth) {
        this.isFullScreenWidth = isFullScreenWidth;
    }

    /**
     * 设置颜色
     *
     * @param devideAndMoneyColor 刻度和刻度金额颜色
     * @param moneyColor          当前金额的颜色
     * @param midLineColor        中线颜色
     * @param titleColor          标题颜色
     */
    public void setColors(int devideAndMoneyColor, int moneyColor, int midLineColor, int titleColor) {
        setDeviDeMoneyColor(devideAndMoneyColor);
        setMoneyColor(moneyColor);
        setMiddleLineColor(midLineColor);
        setTitleColor(titleColor);
    }
}
