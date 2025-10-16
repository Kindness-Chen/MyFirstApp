package com.yitong.android.widget.calendar.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;

import com.yitong.android.widget.calendar.CalendarConstant;
import com.yitong.android.widget.calendar.utils.DayStyle;

/**
 * 日历控件头部绘制类
 */
@SuppressLint("ViewConstructor")
public class DateWidgetDayHeader extends View {
	
	private Paint pt = new Paint();
	
	private RectF rect = new RectF();
	
	private int iWeekDay = -1;
	
	private Activity activity;
	
	private DateWidgetDayHeader view;

	public DateWidgetDayHeader(Activity activity, int iWidth, int iHeight) {
		super(activity);
		this.activity = activity;
		view = this;
		setLayoutParams(new LayoutParams(iWidth, iHeight));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		rect.set(0, 0, this.getWidth(), this.getHeight());// 设置矩形大小
		rect.inset(0, 0);
		drawDayHeader(canvas);// 绘制日历头部
	}

	private void drawDayHeader(final Canvas canvas) {
		activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				// 画矩形，并设置矩形画笔的颜色
				pt.setColor(CalendarConstant.Calendar_WeekBgColor);
				canvas.drawRect(rect, pt);
				// 写入日历头部，设置画笔参数
				pt.setTypeface(null);
				pt.setTextSize(CalendarConstant.fTextSize_header);
				pt.setAntiAlias(true);
				pt.setFakeBoldText(true);
				pt.setColor(CalendarConstant.Calendar_WeekFontColor);
				
				// draw day name
				final String sDayName = DayStyle.getWeekDayName(iWeekDay);
				final int iPosX = (int) rect.left + ((int) rect.width() >> 1)
						- ((int) pt.measureText(sDayName) >> 1);
				final int iPosY = (int) (view.getHeight()
						- (view.getHeight() - getTextHeight()) / 2 - pt
						.getFontMetrics().bottom);
				canvas.drawText(sDayName, iPosX, iPosY, pt);
			}
		});
	}

	// 得到字体高度
	private int getTextHeight() {
		return (int) (-pt.ascent() + pt.descent());
	}

	// 得到一星期的第几天的文本标记
	public void setData(int iWeekDay) {
		this.iWeekDay = iWeekDay;
	}
}