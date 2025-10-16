package com.example.myfirstapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Date：2024/6/7
 * Time：9:15
 * Author：chenshengrui
 */
public class LetterSideBar extends View {

    private Paint mTextPaint, mTouchTextPaint;

    private String[] mLetters = {"A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    private String mCurrentTouchText;

    public LetterSideBar(Context context) {
        this(context, null);
    }

    public LetterSideBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterSideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.BLUE);
        mTextPaint.setTextSize(sp2px(12));

        mTouchTextPaint = new Paint();
        mTouchTextPaint.setAntiAlias(true);
        mTouchTextPaint.setColor(Color.GREEN);
        mTouchTextPaint.setTextSize(sp2px(12));
    }

    private float sp2px(int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //计算宽度 = 左右padding宽度+字体大小宽度（取决于画笔）
        int textWidth = (int) mTextPaint.measureText("A");
        int width = getPaddingLeft() + getPaddingRight() + textWidth;
        //计算高度
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        //字母高度
        int itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / mLetters.length;
        //画26个字母
        for (int i = 0; i < mLetters.length; i++) {
            //知道字母高度 字母高度一半  字母高度一半+前项字母高度
            int letterCenterY = i * itemHeight + itemHeight / 2 + getPaddingTop();
            Paint.FontMetricsInt metricsInt = mTextPaint.getFontMetricsInt();
            int dy = (metricsInt.bottom - metricsInt.top) / 2 - metricsInt.bottom;
            int baseLine = letterCenterY + dy;
            //宽度一半-字母宽度一半
            int x = getWidth() / 2 - (int) mTextPaint.measureText(mLetters[i]) / 2;
            if (mLetters[i].equals(mCurrentTouchText)) {
                canvas.drawText(mLetters[i], x, baseLine, mTouchTextPaint);
            } else {
                canvas.drawText(mLetters[i], x, baseLine, mTextPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                //计算当前触摸的位置
                float currentMoveY = event.getY();
                //字母高度
                int itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / mLetters.length;
                //计算当前触摸的字母
                int currentPosition = (int) currentMoveY / itemHeight;
                if (currentPosition < 0) {
                    currentPosition = 0;
                }

                if (currentPosition > mLetters.length - 1) {
                    currentPosition = mLetters.length - 1;
                }
                mCurrentTouchText = mLetters[currentPosition];

                if (null != mTouchTextListener) {
                    mTouchTextListener.onShowTouchText(mCurrentTouchText, true);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (null != mTouchTextListener) {
                    mTouchTextListener.onShowTouchText(mCurrentTouchText, false);
                }
                mCurrentTouchText = "";
                invalidate();
                break;
        }
        return true;
    }

    private TouchTextListener mTouchTextListener;

    public void setTouchTextListener(TouchTextListener touchTextListener) {
        this.mTouchTextListener = touchTextListener;
    }

    public interface TouchTextListener {
        void onShowTouchText(String touchText, boolean isDown);
    }
}
