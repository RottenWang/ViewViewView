package com.drwang.views.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Keep;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;

public class MaterialEditText extends AppCompatEditText {
    private static final int TOP_OFFSET = (int) dp2px(20);
    private static final int TOP_OFFSET_Y = (int) dp2px(5);
    private static final int LABEL_LEFT_OFFSET = (int) dp2px(4);
    private CharSequence hint;
    private Paint paint;
    private float percent = 0;
    private ObjectAnimator showAnimator;
    private ObjectAnimator hideAnimator;
    private boolean isShow;

    public MaterialEditText(Context context) {
        super(context);

    }

    @Keep
    void setPercent(float percent) {
        this.percent = percent;
        invalidate();
    }

    @Keep
    float getPercent() {
        return percent;
    }

    public MaterialEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MaterialEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        paint = new Paint();
        paint.setTextSize(dp2px(20));
        setPadding(getPaddingLeft(), getPaddingTop() + TOP_OFFSET + TOP_OFFSET_Y, getPaddingRight(), getPaddingBottom());
        hint = getHint();
        showAnimator = ObjectAnimator.ofFloat(this, "percent", 1f);
        hideAnimator = ObjectAnimator.ofFloat(this, "percent", 1f,0f);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && !isShow) {
                    hideAnimator.cancel();
                    showAnimator.start();
                    isShow = true;
                } else if (s.length() == 0) {
                    showAnimator.cancel();
                    isShow = false;
                    hideAnimator.start();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setAlpha((int) (255 * percent));
        canvas.drawText(hint, 0, hint.length(), LABEL_LEFT_OFFSET, TOP_OFFSET + TOP_OFFSET_Y, paint);

    }

    public static float dp2px(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
    }

}
