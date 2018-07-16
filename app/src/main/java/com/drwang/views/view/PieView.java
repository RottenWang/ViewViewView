package com.drwang.views.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by Dr.Wang on 2017/7/6.
 * <p>
 * how to use ?
 * 1.add the view in your layout ;
 * 2.invoke method {@link PieView#addData(String, float)} or {@link PieView#addAllData(Map)}  to set your data
 * 3.invoke method {@link PieView#startDraw()} to draw on the view;
 */

public class PieView extends View {
    private static final String TAG = "pieView";
    private int drawWidth;
    private int drawHeight;
    private Paint mPaint;
    private int paddingBottom;
    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private Map<String, Float> mValueMap;
    private float size;
    PointF pointStart = new PointF();
    PointF pointEnd = new PointF();
    PointF pointCenter = new PointF();
    PointF pointBigAngle = new PointF();
    private double x;
    private double y;
    private Rect rect;
    private DecimalFormat df;
    private RectF rectF;

    public PieView(Context context) {
        super(context);
        init();
    }

    public PieView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PieView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mRandom = new Random();
        mValueMap = new TreeMap<>();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        rect = new Rect();
        df = new DecimalFormat("0.00");
        rectF = new RectF();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        paddingBottom = getPaddingBottom();
        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        paddingTop = getPaddingTop();
        drawWidth = w - (paddingRight > 0 ? paddingRight : 0) - (paddingLeft > 0 ? paddingLeft : 0);
        drawHeight = h - (paddingTop > 0 ? paddingTop : 0) - (paddingBottom > 0 ? paddingBottom : 0);
        if (drawWidth > drawHeight) {
            drawWidth = drawHeight;
        } else if (drawWidth < drawHeight) {
            drawHeight = drawWidth;
        }
        resetPointF();
    }

    private float mStartAngle;
    private Random mRandom;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float allAngle = 360f;
        rectF.set(paddingLeft, paddingTop, drawWidth, drawHeight);
        Set<String> names = mValueMap.keySet();
        float currentAngle;
        mStartAngle = 0;
        for (String name : names) {
            mPaint.setColor(Color.argb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255)));
            currentAngle = allAngle * (mValueMap.get(name) / (mCount * 1.0f));
            if (maxCount ==  mValueMap.get(name)) {
                canvas.save();
                setTransLate(canvas, mStartAngle, currentAngle);

            }
            canvas.drawArc(rectF, mStartAngle, currentAngle, true, mPaint);
            float percent = mValueMap.get(name) / (mCount * 1.0f);

            percent *= 100;
            String percentString = df.format(percent) + "%";
            mStartAngle += currentAngle;
            mStartAngle = mStartAngle > 360 ? 360 : mStartAngle;

            mPaint.setTextSize(size);
            mPaint.getTextBounds(percentString, 0, percentString.length(), rect);
            int halfX = Math.abs(rect.left - rect.right) / 2;
            int halfY = Math.abs(rect.bottom - rect.top) / 2;
            pointStart.x = pointEnd.x;
            pointStart.y = pointEnd.y;
//            底边的长度
            if (mStartAngle < 90) {
                pointEnd.x = (float) (drawWidth / 2 + ((drawWidth / 2) * Math.cos(Math.toRadians(mStartAngle))));
                pointEnd.y = (float) ((drawHeight / 2) + ((drawWidth / 2) * Math.sin(Math.toRadians(mStartAngle))));
            } else if (mStartAngle == 90) {
                pointEnd.x = drawWidth / 2;
                pointEnd.y = drawHeight;
            } else if (mStartAngle > 90 && mStartAngle < 180) {
                pointEnd.x = (float) (drawWidth / 2 - (drawWidth / 2 * Math.sin(Math.toRadians(mStartAngle - 90))));
                pointEnd.y = (float) (drawWidth / 2 + (drawWidth / 2 * Math.cos(Math.toRadians(mStartAngle - 90))));
            } else if (mStartAngle == 180) {
                pointEnd.x = paddingLeft;
                pointEnd.y = drawHeight / 2;
            } else if (mStartAngle > 180 && mStartAngle < 270) {
                pointEnd.x = (float) (drawWidth / 2 - (drawWidth / 2 * Math.cos(Math.toRadians(mStartAngle - 180))));
                pointEnd.y = (float) (drawWidth / 2 - (drawWidth / 2 * Math.sin(Math.toRadians(mStartAngle - 180))));
            } else if (mStartAngle == 270) {
                pointEnd.x = drawWidth / 2;
                pointEnd.y = paddingTop;
            } else if (mStartAngle > 270 && mStartAngle < 360) {
                pointEnd.x = (float) (drawWidth / 2 + (drawWidth / 2 * Math.sin(Math.toRadians(mStartAngle - 270))));
                pointEnd.y = (float) (drawHeight / 2 - (drawWidth / 2 * Math.cos(Math.toRadians(mStartAngle - 270))));
            } else if (mStartAngle == 360) {
                pointEnd.x = drawWidth;
                pointEnd.y = drawHeight / 2;
            }
            float textX;
            float textY;
            if (currentAngle < 180) {
                textX = ((pointStart.x + pointEnd.x + pointCenter.x) / 3.0f) - halfX;
                textY = ((pointStart.y + pointEnd.y + pointCenter.y) / 3.0f) + halfY;
            } else {
                float halfAngle = mStartAngle - (currentAngle / 2);
                if (halfAngle < 90) {
                    pointBigAngle.x = (float) ((drawWidth / 2 * Math.cos(Math.toRadians(halfAngle))) + drawHeight / 2);
                    pointBigAngle.y = (float) ((drawWidth / 2 * Math.sin(Math.toRadians(halfAngle))) + drawWidth / 2);
                } else if (halfAngle == 90) {
                    pointBigAngle.x = drawWidth / 2;
                    pointBigAngle.y = drawHeight;
                } else if (halfAngle > 90 && halfAngle < 180) {
                    pointBigAngle.x = (float) (drawWidth / 2 - (drawWidth / 2 * Math.sin(Math.toRadians(halfAngle - 90))));
                    pointBigAngle.y = (float) (drawWidth / 2 + (drawWidth / 2 * Math.cos(Math.toRadians(halfAngle - 90))));
                } else if (halfAngle == 180) {
                    pointBigAngle.x = paddingLeft;
                    pointBigAngle.y = drawHeight / 2;
                } else if (halfAngle > 180 && halfAngle < 270) {
                    pointBigAngle.x = (float) (drawWidth / 2 - (drawWidth / 2 * Math.cos(Math.toRadians(halfAngle - 180))));
                    pointBigAngle.y = (float) (drawWidth / 2 - (drawWidth / 2 * Math.sin(Math.toRadians(halfAngle - 180))));
                } else if (halfAngle == 270) {
                    pointBigAngle.x = drawWidth / 2;
                    pointBigAngle.y = paddingTop;
                } else if (halfAngle > 270 && halfAngle < 360) {
                    pointBigAngle.x = (float) (drawWidth / 2 + (drawWidth / 2 * Math.sin(Math.toRadians(halfAngle - 270))));
                    pointBigAngle.y = (float) (drawHeight / 2 - (drawWidth / 2 * Math.cos(Math.toRadians(halfAngle - 270))));
                } else if (halfAngle == 360) {
                    pointBigAngle.x = drawWidth;
                    pointBigAngle.y = drawHeight / 2;
                }
                textX = (pointBigAngle.x + pointCenter.x) / 2;
                textY = (pointBigAngle.y + pointCenter.y) / 2;
            }
            mPaint.setColor(Color.BLACK);
            canvas.drawText(percentString, 0, percentString.length(), textX, textY, mPaint);
            if (maxCount ==  mValueMap.get(name)) {
                canvas.restore();
            }
        }

    }

    /**
     * 设置偏移
     *
     * @param canvas
     * @param startAngle
     * @param currentAngle
     */
    private void setTransLate(Canvas canvas, float startAngle, float currentAngle) {
        float half = currentAngle / 2f;
        float transLength = 10;
        PointF pointF = new PointF();
        //这里判断应该用startAngle 先判断所在象限 因为此时默认未0  所有不做判断
        if (currentAngle <= 90) {
            pointF.x = (float) (transLength * Math.cos(Math.toRadians(half)));
            pointF.y = (float) (transLength * Math.sin(Math.toRadians(half)));
        } else if (currentAngle == 180) {
            pointF.x = transLength;
            pointF.y = transLength;
        } else if (currentAngle < 360) {
            pointF.x = -(float) (transLength * Math.sin(Math.toRadians(half - 90)));
            pointF.y = (float) (transLength * Math.cos(Math.toRadians(half - 90)));
        } else if (currentAngle == 360) {

        }
        canvas.translate(pointF.x, pointF.y);
    }


    /**
     * add your data use special name and count if name has already exists,the count will be replaced;
     *
     * @param name  the special name what you set;
     * @param count the number;
     */
    public void addData(String name, float count) {
        mValueMap.put(name, count);
    }

    /**
     * remove the special data by name;
     *
     * @param name the data's name what you want remove;
     */
    public void removeData(String name) {
        mValueMap.remove(name);
    }

    /**
     * add all your data with the special name and count;
     *
     * @param map your data map;
     */
    public void addAllData(Map<String, Float> map) {
        mValueMap.putAll(map);
    }

    /**
     * remove all your data;
     */
    public void removeAllData() {
        mValueMap.clear();
    }

    private float mCount;
    float maxCount = 0;
    /**
     * calculator all data;
     */
    private void calculatorAllPercentAndSweepAngle() {
        Set<String> names = mValueMap.keySet();
        for (String name : names) {
            float count = mValueMap.get(name);
            mCount += count;
            maxCount = count > maxCount ? count : maxCount;
        }
    }

    /**
     * this method must invoke when you changes the data; for example {@link PieView#addAllData(Map)}
     */
    public void startDraw() {
        mCount = 0;
        calculatorAllPercentAndSweepAngle();
        resetPointF();
        requestLayout();
        invalidate();
    }

    private void resetPointF() {
        size = drawHeight / 25;
        //initialize pointF
        pointCenter.x = drawWidth / 2;
        pointCenter.y = drawHeight / 2;
        pointEnd.x = drawWidth;
        pointEnd.y = drawHeight / 2;
        mStartAngle = 0;
    }


}
