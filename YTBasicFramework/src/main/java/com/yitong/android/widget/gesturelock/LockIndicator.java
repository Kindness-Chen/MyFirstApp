package com.yitong.android.widget.gesturelock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.yitong.basic.R;

/**
 * Created by Jeremy on 2017/7/10.
 */

public class LockIndicator extends View {

    private int numRow = 3;
    private int numColum = 3;
    private int patternWidth = 40;
    private int patternHeight = 40;
    private int f = 5;
    private int g = 5;
    private int strokeWidth = 3;
    private Paint paint = null;
    private Drawable patternNormal = null;
    private Drawable patternPressed = null;
    private String lockPassStr;

    public LockIndicator(Context paramContext) {
        super(paramContext);
    }

    public LockIndicator(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet, 0);
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.paint.setStrokeWidth((float) this.strokeWidth);
        this.paint.setStyle(Paint.Style.STROKE);
        this.patternNormal = this.getResources().getDrawable(R.drawable.lock_pattern_node_normal);
        this.patternPressed = this.getResources().getDrawable(R.drawable.lock_pattern_node_pressed);
        if (this.patternPressed != null) {
            this.patternWidth = this.patternPressed.getIntrinsicWidth();
            this.patternHeight = this.patternPressed.getIntrinsicHeight();
            this.f = this.patternWidth / 4;
            this.g = this.patternHeight / 4;
            this.patternPressed.setBounds(0, 0, this.patternWidth, this.patternHeight);
            this.patternNormal.setBounds(0, 0, this.patternWidth, this.patternHeight);
        }

    }

    protected void onDraw(Canvas canvas) {
        if (this.patternPressed != null && this.patternNormal != null) {
            for (int i = 0; i < this.numRow; ++i) {
                for (int j = 0; j < this.numColum; ++j) {
                    this.paint.setColor(Color.GRAY);
                    int i1 = j * this.patternHeight + j * this.g;
                    int i2 = i * this.patternWidth + i * this.f;
                    canvas.save();
                    canvas.translate((float) i1, (float) i2);
                    String curNum = String.valueOf(this.numColum * i + j + 1);
                    if (!TextUtils.isEmpty(this.lockPassStr)) {
                        if (!this.lockPassStr.contains(curNum)) {
                            this.patternNormal.draw(canvas);
                        } else {
                            this.patternPressed.draw(canvas);
                        }
                    } else {
                        this.patternNormal.draw(canvas);
                    }

                    canvas.restore();
                }
            }
        }

    }

    protected void onMeasure(int paramInt1, int paramInt2) {
        if (this.patternPressed != null) {
            this.setMeasuredDimension(this.numColum * this.patternHeight + this.g * (-1 + this.numColum), this.numRow * this.patternWidth + this.f * (-1 + this.numRow));
        }

    }

    public void setPath(String paramString) {
        this.lockPassStr = paramString;
        this.invalidate();
    }
}
