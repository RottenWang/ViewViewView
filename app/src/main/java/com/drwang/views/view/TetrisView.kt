package com.drwang.views.view

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import com.drwang.views.util.DensityUtil
import java.util.*
import kotlin.collections.ArrayList

class TetrisView(context: Context, attributes: AttributeSet) : View(context, attributes) {
    var speed: Long = 1000
    var currentSpeed: Long = speed
    var path: Path = Path()

    companion object {
        const val SPEED_UP_LEFT = 1
        const val SPEED_UP_RIGHT = 2
        const val CURRENT_EVENT = 0
        const val CURRENT_CHANGE_DOWN_SPEED = 3
    }

    data class Square(var left: Int = 0, var top: Int = 0, var right: Int = 0, var bottom: Int = 0, var color: Int = Color.RED) {
        fun less() {
            top--
            bottom--
        }

        override fun equals(other: Any?): Boolean {
            val square = other as? Square
            return square?.left == this.left
        }
    }

    var mHandler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                CURRENT_CHANGE_DOWN_SPEED ->{
                    currentSpeed = currentSpeed / 8
                }
                CURRENT_EVENT -> {
                    updateInprogressSquare()
                    postInvalidate()
                    sendEmptyMessageDelayed(CURRENT_EVENT, currentSpeed)
                }
                SPEED_UP_LEFT -> {
                    changeInprogressSquare(-1)
                    sendEmptyMessageDelayed(SPEED_UP_LEFT, currentSpeed / 8)
                }
                SPEED_UP_RIGHT -> {
                    changeInprogressSquare(1)
                    sendEmptyMessageDelayed(SPEED_UP_RIGHT, currentSpeed / 8)
                }
            }

        }
    }

    private fun updateInprogressSquare() {
        inprogressSquare?.forEach {
            it.less()
        }

    }

    var paint: Paint? = null
    var tetrisWidth: Int = 0
    //一排有几个方块
    var widthCount = 10
    //控件高度最多可以放下多少个方块
    var heightCount = 0
    //竖排
    var verticalList: ArrayList<ArrayList<Square>> = ArrayList()
    //横排
    var horizontal: ArrayList<ArrayList<Square>> = ArrayList()
    var inprogressSquare: ArrayList<Square>? = null
    //生成方块
    fun generateSquare(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0, color: Int): Square {
        return Square(left, top, right, bottom, color)
    }

    //生成直线方块
    fun generateLineSquare(): ArrayList<Square> {
        var lineList: ArrayList<Square> = ArrayList()
        val left = widthCount / 2
        val right = left + 1
        var bottom = heightCount - 4
        var top = bottom + 1
        val color = Color.rgb(Random().nextInt(255), Random().nextInt(255), Random().nextInt(255))
        lineList.add(generateSquare(left, top++, right, bottom++, color))
        lineList.add(generateSquare(left, top++, right, bottom++, color))
        lineList.add(generateSquare(left, top++, right, bottom++, color))
        lineList.add(generateSquare(left, top, right, bottom, color))
        return lineList
    }


    fun changeInprogressSquare(horizontal: Int) {
        var canMove: Boolean = true
        inprogressSquare?.forEach {
            //判断移动后 是否会造成重叠
            val i = it.left + horizontal
            if (i < 0 || i > widthCount) {
                return@forEach
            }
            verticalList.get(i).forEach { vertical ->
                //当前位置有方块 不可移动
                if (vertical.bottom == it.bottom) {
                    canMove = false
                    return@forEach
                }
            }

        }
        if (!canMove) {
            return
        }
        inprogressSquare?.forEach {
            it.left += horizontal
            it.right += horizontal
            if (it.left < 0) {
                it.left = 0
                it.right = 1
            }
            if (it.right > widthCount) {
                it.right = widthCount
                it.left = widthCount - 1
            }
        }
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas) {
        //检查当前是否已经到达底部
        if (inprogressSquare == null) {
            inprogressSquare = generateLineSquare()
        }
        checkInprogressLocation()
        inprogressSquare?.forEach {
            drawSquare(canvas, it)

        }
        //绘制竖排数据
        verticalList.forEach {
            it.forEach {
                drawSquare(canvas, it)
            }
        }
    }

    private fun drawSquare(canvas: Canvas, it: Square) {
        if (paint == null) {
            paint = Paint(Paint.ANTI_ALIAS_FLAG)
            paint?.strokeWidth = DensityUtil.getInstance().getDensity(context) * 2
        }
        paint?.alpha = 255
        paint?.color = it.color
        val drawRect = getDrawRect(it)
        //画边界
        paint?.style = Paint.Style.STROKE

        drawRect.left = drawRect.left + DensityUtil.getInstance().getDensity(context) * 2
        drawRect.right = drawRect.right - DensityUtil.getInstance().getDensity(context) * 2
        drawRect.top = drawRect.top + DensityUtil.getInstance().getDensity(context) * 2
        drawRect.bottom = drawRect.bottom - DensityUtil.getInstance().getDensity(context) * 2
        canvas.drawRect(drawRect, paint)

        drawRect.left = drawRect.left + DensityUtil.getInstance().getDensity(context) * 2
        drawRect.right = drawRect.right - DensityUtil.getInstance().getDensity(context) * 2
        drawRect.top = drawRect.top + DensityUtil.getInstance().getDensity(context) * 2
        drawRect.bottom = drawRect.bottom - DensityUtil.getInstance().getDensity(context) * 2
        paint?.alpha = 155
        paint?.style = Paint.Style.FILL
        canvas.drawRect(drawRect, paint)
//        path.addRect(drawRect, Path.Direction.CW)
    }

    private fun checkInprogressLocation(): Boolean {
        //获取inprogressSquare四个方块的位置信息
        val inprogressSquareLocation = getInprogressSquareLocation()
        //根据已有的数据 去判断当前的bottom信息
        var isNeedFinish: Boolean = false
        if (inprogressSquareLocation.isEmpty()) {
            return false
        }
        inprogressSquareLocation.forEach {
            //获取到当前位置的 已有的Square信息
            val get = verticalList.get(it.left)
            if (get.isEmpty()) {
                //无数据
                if (it.bottom == 0) {
                    isNeedFinish = true
                    return@forEach
                }
            } else {
                get.forEach { current ->
                    //判断已有数据中top是否和新数据bottom重叠
                    if (current.top == it.bottom) {
                        isNeedFinish = true
                    }
                    if (current.bottom >= it.bottom) {
                        mHandler.removeMessages(CURRENT_EVENT)
                        mHandler.removeCallbacksAndMessages(null)
                        Toast.makeText(context, "输掉了", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }
        if (isNeedFinish) {
            //重置速度
            currentSpeed = speed
            //需要结束 将数据放入竖排数据,横排数据中
            inprogressSquare?.forEach {
                val get = verticalList.get(it.left)
                get.add(it)
                val get1 = horizontal.get(it.bottom)
                get1.add(it)
            }
            inprogressSquare = null
            //判断横排数据是否有满足条件的
            var y: Int = 0
            for (j in horizontal.indices) {
                //拿到横排数据
                val arrayList = horizontal[j]
                //此排数据满了
                if (arrayList.size == widthCount) {
                    y++
                    arrayList.forEach {
                        //移除对应竖排中的数据
                        val get = verticalList.get(it.left)
                        get.remove(it)
                    }
                    //移除自身数据
                    arrayList.clear()
                    //对剩余数据进行处理
                    verticalList.forEach {
                        it.forEach { r ->
                            if (r.bottom > j - y) {
                                //向下移动一行的位置
                                r.bottom--
                                r.top--
                            } else {
                                //删除的数据在此方块下方,或者已经到对应位置 无视
                            }
                        }
                    }
                }
            }

        }
        return isNeedFinish
    }

    private fun getInprogressSquareLocation(): ArrayList<Square> {
        val arrayList = ArrayList<Square>()
        inprogressSquare?.forEach {
            //判断是否包含此位置的Square
            if (arrayList.contains(it)) {
                //包含 去判断两者位置信息
                for (i in arrayList.indices) {
                    val square = arrayList[i]
                    if (square.left == it.left) {
                        //判断bottom信息 使在最下面的保存在位置数据中
                        if (square.bottom > it.bottom) {
                            arrayList.remove(square)
                            arrayList.add(it)
                            break
                        }
                    }
                }
            } else {
                arrayList.add(it)
            }
        }
        return arrayList
    }

    private fun getDrawRect(it: Square): RectF {
        val rect = RectF()
        rect.left = it.left * tetrisWidth.toFloat()
        rect.right = rect.left + tetrisWidth
        rect.top = measuredHeight - it.top * tetrisWidth.toFloat()
        rect.bottom = measuredHeight - it.bottom * tetrisWidth.toFloat()
        return rect
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        calculaterTetris()
    }

    private fun calculaterTetris() {
        tetrisWidth = measuredWidth / widthCount
        heightCount = measuredHeight / tetrisWidth
        for (i in 0 until widthCount) {
            verticalList.add(ArrayList())
        }
        for (i in 0 until heightCount) {
            horizontal.add(ArrayList())
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mHandler.sendEmptyMessageDelayed(CURRENT_EVENT, currentSpeed)
    }

    fun changeSpeedLeft() {
        //改变移动速度
        mHandler.sendEmptyMessageDelayed(SPEED_UP_LEFT, 500)
    }

    fun changeSpeedRight() {
        //改变移动速度
        mHandler.sendEmptyMessageDelayed(SPEED_UP_RIGHT, 500)
    }

    fun resetSpeed() {
        mHandler.removeMessages(SPEED_UP_LEFT)
        mHandler.removeMessages(SPEED_UP_RIGHT)
    }

}