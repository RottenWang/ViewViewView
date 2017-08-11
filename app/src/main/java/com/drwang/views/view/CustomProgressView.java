package com.drwang.views.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.drwang.views.R;

/**
 * Created by Administrator on 2017/8/11.
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class CustomProgressView extends View {
    private Paint paint; //画笔
    private int color; //圆角背景的颜色
    private int textColor;//文字的颜色
    private Path path;
    private Path path2;
    private float density;//dp比例
    Rect rect;//文字边界rect

    public CustomProgressView(Context context) {
        this(context, null);
    }

    public CustomProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private float percent; //当前值
    private float percentAll = 5.0f;  //最大的值
    private float roundRadius; //圆角角度值

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG); //抗锯齿
        density = getResources().getDisplayMetrics().density; //dp比例
        path = new Path(); //path
        path2 = new Path();
        rect = new Rect(); //文字边界rect
        reset(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth(); //view的宽
        int height = getHeight();//view的高
        RectF rectF = new RectF(0, 0, width, height);
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL); //设置模式为填充模式
        path.reset(); // 重置path
        path2.reset();
        path2.addRect(new RectF(width / percentAll * percent, 0, width, height), Path.Direction.CW);//添加矩形到path2
        path.addRoundRect(rectF, roundRadius, roundRadius, Path.Direction.CW);//添加圆角矩形到path
        path.op(path2, Path.Op.DIFFERENCE);//计算path和path2的不同的地方并返回给path
        //画百分比圆条形
        canvas.drawPath(path, paint);//按path去画
        paint.setStyle(Paint.Style.STROKE);//设置模式为线条模式
        //画圆角边界
        canvas.drawRoundRect(rectF, roundRadius, roundRadius, paint);
        String text = String.valueOf(percent);//生成文字
        paint.setColor(textColor);//设置文本颜色
        paint.getTextBounds(text, 0, text.length(), rect); //获取文字边界
        float lineX = width / 2 - (rect.left + rect.right) / 2.0f; // 计算文字的基准线
        float lineY = height / 2 - (rect.top + rect.bottom) / 2.0f;
        //画文字
        canvas.drawText(text, lineX, lineY, paint);

    }

    /**
     * 设置百分比  范围 0.0f -- 5.0f{@link CustomProgressView#percentAll}
     *
     * @param percent
     */
    public void setPercent(float percent) {
        this.percent = percent;
        invalidate();
    }

    /**
     * @param color 文字颜色的 color res
     */
    public void setTextColor(@ColorRes int color) {
        this.textColor = getResources().getColor(color);
        invalidate();
    }

    /**
     * @param textColor 文字颜色的string值 如#ffffffff
     */
    public void setTextColor(String textColor) {
        this.textColor = Color.parseColor(textColor);
        invalidate();
    }

    /**
     * @param color 设置圆角背景的颜色 color的res
     */
    public void setCustomBackGroundColor(@ColorRes int color) {
        this.color = getResources().getColor(color);
        invalidate();
    }

    /**
     * @param color 设置圆角背景的颜色 string值
     */
    public void setCustomBackGroundColor(String color) {
        this.color = Color.parseColor(color);
        invalidate();
    }

    public void setTextSize(int dp) {
        paint.setTextSize(dp * density);
        invalidate();
    }


    /**
     * 重置初始化状态
     */
    public void reset() {
        reset(false);
    }


    private void reset(boolean isInit) {
        percent = 2.5f; //设置默认为2.5f
        color = Color.parseColor("#683100"); //默认背景颜色
        paint.setTextSize(9 * density); //设置文字大小
        roundRadius = 5 * density; //圆角大小
        textColor = Color.parseColor("#A6FFFFFF"); //默认文字颜色 白色 60透明度
        if (!isInit) {
            invalidate();
        }
    }
}
