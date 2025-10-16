package com.example.myfirstapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myfirstapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Date：2024/6/6
 * Time：14:19
 * Author：chenshengrui
 */
public class StepView extends View {

    //圆宽度
    private int borderWidth = 0;

    //外圆颜色
    private int outerCircleColor = Color.BLUE;

    //内圆颜色
    private int innerCircleColor = Color.YELLOW;

    //步数文字大小
    private int stepCountTextSize = 0;

    //步数文字颜色
    private int stepCountTextColor = Color.BLUE;

    private int Max = 100;
    private int progress = 50;

    private Paint innerPaint, outerPaint, textPaint;

    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StepView);
        borderWidth = (int) typedArray.getDimension(R.styleable.StepView_border_width, dip2px(15));
        outerCircleColor = typedArray.getColor(R.styleable.StepView_outer_circle_color, outerCircleColor);
        innerCircleColor = typedArray.getColor(R.styleable.StepView_inner_circle_color, innerCircleColor);
        stepCountTextSize = (int) typedArray.getDimension(R.styleable.StepView_step_count_text_size, sp2px(10));
        stepCountTextColor = typedArray.getColor(R.styleable.StepView_step_count_text_color, stepCountTextColor);

        typedArray.recycle();

        innerPaint = new Paint();
        innerPaint.setAntiAlias(true);
        innerPaint.setColor(innerCircleColor);
        innerPaint.setStrokeWidth(borderWidth);
        innerPaint.setStyle(Paint.Style.STROKE);

        outerPaint = new Paint();
        outerPaint.setAntiAlias(true);
        outerPaint.setColor(outerCircleColor);
        outerPaint.setStrokeWidth(borderWidth);
        outerPaint.setStyle(Paint.Style.STROKE);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(stepCountTextColor);
        textPaint.setTextSize(stepCountTextSize);
    }


    //sp转px
    private float sp2px(int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    //dip转px
    private float dip2px(int dip) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //只保证是正方形
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(Math.min(width, height), Math.min(width, height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //自己画，需要注释super
//        super.onDraw(canvas);

        //画内圆
        int center = getWidth() / 2;
        canvas.drawCircle(center, center, center - borderWidth / 2, innerPaint);

        //画外圆，画圆弧
        if (progress == 0) {
            return;
        }
        RectF rectF = new RectF(0 + borderWidth / 2, 0 + borderWidth / 2, getWidth() - borderWidth / 2, getWidth() - borderWidth / 2);
        float percent = (float) progress / Max;
        canvas.drawArc(rectF, 0, percent * 360, false, outerPaint);

        //画文字
        String text = ((int) (percent * 100)) + "%";
        Rect rect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), rect);
        int dx = getWidth() / 2 - rect.width() / 2;
        Paint.FontMetricsInt fontMetricsInt = textPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int baseLine = getHeight() / 2 + dy;
//        int baseLine = getHeight() / 2 + getBaseline();
        canvas.drawText(text, dx, baseLine, textPaint);
    }

    public void setMax(int max) {
        this.Max = max;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }
}
