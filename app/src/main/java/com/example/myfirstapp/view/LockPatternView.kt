package com.example.myfirstapp.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.net.DatagramPacket
import java.net.DatagramSocket
import kotlin.math.sqrt

/**
 * Date：2024/6/10
 * Time：11:05
 * Author：chenshengrui
 */
class LockPatternView : View {

    // 二维数组初始化，int[3][3]
    private var mPoints: Array<Array<Point?>> = Array(3) { arrayOfNulls<Point?>(3) }

    // 是否初始化
    private var mIsInit = false

    //外圆的半径
    private var mDotRadius = 0

    // 画笔
    private lateinit var mLinePaint: Paint
    private lateinit var mPressedPaint: Paint
    private lateinit var mErrorPaint: Paint
    private lateinit var mNormalPaint: Paint
    private lateinit var mArrowPaint: Paint

    // 颜色
    private val mOuterPressedColor = 0xff8cbad8.toInt()
    private val mInnerPressedColor = 0xff0596f6.toInt()
    private val mOuterNormalColor = 0xffd9d9d9.toInt()
    private val mInnerNormalColor = 0xff929292.toInt()
    private val mOuterErrorColor = 0xff901032.toInt()
    private val mInnerErrorColor = 0xffea0945.toInt()

    //按下的时候是否在一个点上
    private var mIsTouchPoint = false

    //有效的集合点
    private var mSelectPoints = ArrayList<Point>()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        //        super.onDraw(canvas)
        //绘制9个点
        if (!mIsInit) {
            initPoints()
            initPaint()
        }

        //绘制九宫格
        drawShow(canvas)
    }

    private var mMovingX = 0f
    private var mMovingY = 0f
    override fun onTouchEvent(event: MotionEvent): Boolean {
        mMovingX = event.x
        mMovingY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                //判断一个点是否在圆里面 点到圆心的距离小于半径
                var point = point
                if (point != null) {
                    mIsTouchPoint = true
                    mSelectPoints.add(point)
                    //改变当前点的状态
                    point.setStatusPressed()
                    updatePointsArray(point)
                }
                //                point?.apply {
                //                    mIsTouchPoint = true
                //                    mSelectPoints.add(this)
                //                    setStatusPressed()
                //                }

            }

            MotionEvent.ACTION_MOVE -> {
                if (mIsTouchPoint) {
                    //手指一定要按在点上
                    var point = point
                    if (point != null) {
                        if (!mSelectPoints.contains(point)) {
                            mSelectPoints.add(point)
                        }
                        //改变当前状态
                        point.statusIsPressed()
                        updatePointsArray(point)
                    }
                }
            }

            MotionEvent.ACTION_UP -> {
                mSelectPoints.clear()
                mIsTouchPoint = false
                //回调密码监听

            }

        }
        invalidate()
        return true
    }

    private val point: Point?
        get() {
            for (i in 0..2) {
                for (point in mPoints[i]) {
                    //判断当前位置是否在圆里面
                    if (checkInRound(point!!.centerX.toFloat(), point.centerY.toFloat(), mDotRadius.toFloat(), mMovingX, mMovingY)) {
                        return point
                    }
                }

            }
            return null
        }

    // 更新 mPoints 数组中对应位置的值
    private fun updatePointsArray(point: Point) {
        for (i in 0..2) {
            for (j in 0..2) {
                if (mPoints[i][j]!!.index == point.index) {
                    mPoints[i][j] = point
                    // 手动重新赋值以确保同步
                    mPoints = mPoints.copyOf()
                    return
                }
            }
        }
    }

    private fun checkInRound(sx: Float, sy: Float, r: Float, x: Float, y: Float): Boolean {
        //x平方 + y平方 的开根号，是否小于半径
        return sqrt((sx - x) * (sx - x) + (sy - y) * (sy - y)) < r
    }

    /**
     * 绘制九宫格
     */
    private fun drawShow(canvas: Canvas) {
        for (i in 0..2) {
            for (point in mPoints[i]) {
                if (point!!.statusIsPressed()) {
                    //先绘制外圆
                    mPressedPaint.color = mOuterPressedColor
                    canvas.drawCircle(point.centerX.toFloat(), point.centerY.toFloat(), mDotRadius.toFloat(), mPressedPaint)
                    //后绘制外圆
                    mPressedPaint.color = mInnerPressedColor
                    canvas.drawCircle(point.centerX.toFloat(), point.centerY.toFloat(), mDotRadius / 6.toFloat(), mPressedPaint)

                }
                if (point.statusIsNormal()) {
                    //先绘制外圆
                    mNormalPaint.color = mOuterNormalColor
                    canvas.drawCircle(point.centerX.toFloat(), point.centerY.toFloat(), mDotRadius.toFloat(), mNormalPaint)
                    //后绘制外圆
                    mNormalPaint.color = mInnerNormalColor
                    canvas.drawCircle(point.centerX.toFloat(), point.centerY.toFloat(), mDotRadius / 6.toFloat(), mNormalPaint)

                }
                if (point.statusIsError()) {
                    //先绘制外圆
                    mErrorPaint.color = mOuterErrorColor
                    canvas.drawCircle(point.centerX.toFloat(), point.centerY.toFloat(), mDotRadius.toFloat(), mErrorPaint)
                    //后绘制外圆
                    mErrorPaint.color = mInnerErrorColor
                    canvas.drawCircle(point.centerX.toFloat(), point.centerY.toFloat(), mDotRadius / 6.toFloat(), mErrorPaint)

                }
            }

            val data = DatagramPacket(ByteArray(1024), 1024)
            val socket2 = DatagramSocket(56789)
            socket2.receive(data)



        }
    }


    /**
     * 初始化画笔
     */
    private fun initPaint() {
        // 线的画笔
        mLinePaint = Paint()
        mLinePaint.color = mInnerPressedColor
        mLinePaint.style = Paint.Style.STROKE
        mLinePaint.isAntiAlias = true
        mLinePaint.strokeWidth = (mDotRadius / 9).toFloat()
        // 按下的画笔
        mPressedPaint = Paint()
        mPressedPaint.style = Paint.Style.STROKE
        mPressedPaint.isAntiAlias = true
        mPressedPaint.strokeWidth = (mDotRadius / 6).toFloat()
        // 错误的画笔
        mErrorPaint = Paint()
        mErrorPaint.style = Paint.Style.STROKE
        mErrorPaint.isAntiAlias = true
        mErrorPaint.strokeWidth = (mDotRadius / 6).toFloat()
        // 默认的画笔
        mNormalPaint = Paint()
        mNormalPaint.style = Paint.Style.STROKE
        mNormalPaint.isAntiAlias = true
        mNormalPaint.strokeWidth = (mDotRadius / 9).toFloat()
        // 箭头的画笔
        mArrowPaint = Paint()
        mArrowPaint.color = mInnerPressedColor
        mArrowPaint.style = Paint.Style.FILL
        mArrowPaint.isAntiAlias = true
    }

    /**
     * 初始化点
     */
    private fun initPoints() {
        //九宫格 存到集合 point[3][3]
        //不断绘制的这九个点都有状态，而且后面肯定需要回调密码都有下标 点肯定是一个对象
        //计算中心位置
        var width = this.width
        var height = this.height

        //兼容横竖屏
        var offsetX = 0
        var offsetY = 0
        if (height > width) {
            //竖屏
            offsetY = (height - width) / 2
            height = width
        } else {
            //横屏
            offsetX = (width - height) / 2
            width = height
        }

        var squareWidth = width / 3

        //外圆的大小
        mDotRadius = width / 12

        mPoints[0][0] = Point(offsetX + squareWidth / 2, offsetY + squareWidth / 2, 0)
        mPoints[0][1] = Point(offsetX + squareWidth * 3 / 2, offsetY + squareWidth / 2, 1)
        mPoints[0][2] = Point(offsetX + squareWidth * 5 / 2, offsetY + squareWidth / 2, 2)
        mPoints[1][0] = Point(offsetX + squareWidth / 2, offsetY + squareWidth * 3 / 2, 3)
        mPoints[1][1] = Point(offsetX + squareWidth * 3 / 2, offsetY + squareWidth * 3 / 2, 4)
        mPoints[1][2] = Point(offsetX + squareWidth * 5 / 2, offsetY + squareWidth * 3 / 2, 5)
        mPoints[2][0] = Point(offsetX + squareWidth / 2, offsetY + squareWidth * 5 / 2, 6)
        mPoints[2][1] = Point(offsetX + squareWidth * 3 / 2, offsetY + squareWidth * 5 / 2, 7)
        mPoints[2][2] = Point(offsetX + squareWidth * 5 / 2, offsetY + squareWidth * 5 / 2, 8)
    }

    class Point(var centerX: Int, var centerY: Int, var index: Int) {
        private val STATUS_NORMAL = 1
        private val STATUS_PRESSED = 2
        private val STATUS_ERROR = 3

        //当前的状态 有三种状态
        private var status = STATUS_NORMAL

        fun setStatusPressed() {
            status = STATUS_PRESSED
        }

        fun setStatusNormal() {
            status = STATUS_NORMAL
        }

        fun setStatusError() {
            status = STATUS_ERROR
        }

        fun statusIsPressed(): Boolean {
            return status == STATUS_PRESSED
        }

        fun statusIsNormal(): Boolean {
            return status == STATUS_NORMAL
        }

        fun statusIsError(): Boolean {
            return status == STATUS_ERROR
        }

    }
}