package com.drwang.views.activity

import android.view.MotionEvent
import com.drwang.views.R
import com.drwang.views.base.BasicActivity
import kotlinx.android.synthetic.main.activity_text.*

class TextActivity : BasicActivity() {
    override fun setContentViewRes(): Int {

        return R.layout.activity_text;
    }

    override fun initializeView() {
        tvLeft.setOnTouchListener { v, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    tv.changeInprogressSquare(-1)
                    tv.changeSpeedLeft()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    tv.resetSpeed()
                }

            }
            true
        }
        tvRight.setOnTouchListener { v, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    tv.changeInprogressSquare(1)
                    tv.changeSpeedRight()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    tv.resetSpeed()
                }

            }
            true
        }
    }

    override fun initializeData() {
    }
}