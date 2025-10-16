package com.yitong.android.widget.calendar.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout.LayoutParams;

import com.yitong.android.widget.calendar.CalendarConstant;
import com.yitong.basic.R;

/**
 * 日历控件单元格绘制类
 */
@SuppressLint("ViewConstructor")
public class DateWidgetDayCell extends View {

	// 基本元素
	private OnItemClick itemClick = null;
	private Paint pt = new Paint();
	private RectF rect = new RectF();
	private String sDate = "";

	// 当前日期
	private int iDateDay = 0;

	// 布尔变量
	private boolean bSelected = false;
	private boolean isSelectCurrentMonth = false;
	private boolean bToday = false;
	private boolean bTouchedDown = false;

	private boolean isShowBlue = false;
	private boolean isShowRed = false;

	private Activity activity;

	public interface OnItemClick {
		public void OnClick(DateWidgetDayCell item);
	}

	// 构造函数
	public DateWidgetDayCell(Activity activity, int iWidth, int iHeight) {
        super(activity);
        this.activity = activity;
		setFocusable(true);
		setLayoutParams(new LayoutParams(iWidth, iHeight));
	}


	// 设置变量值
	public void setData(int iMonth, int iDay, Boolean bToday,
			boolean isSelectCurrentMonth, boolean isShowBlue, boolean isShowRed) {
		iDateDay = iDay;
		this.sDate = Integer.toString(iDateDay);
		this.isSelectCurrentMonth = isSelectCurrentMonth;
		this.bToday = bToday;
		this.isShowBlue = isShowBlue;
		this.isShowRed = isShowRed;
	}

	// 重载绘制方法
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		rect.set(0, 0, this.getWidth(), this.getHeight());
		rect.inset(10, 10);
		final boolean bFocused = IsViewFocused();
		drawDayView(canvas, bFocused, isShowBlue, isShowRed);
		drawDayNumber(canvas);
	}

	public boolean IsViewFocused() {
		return (this.isFocused() || bTouchedDown);
	}

	public void drawBlue(final Canvas canvas) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Paint pt3 = new Paint();
				pt3.setColor(getResources().getColor(R.color.moneyin));
				pt3.setStyle(Paint.Style.FILL);
				canvas.drawCircle(rect.right*2 / 5, rect.bottom * 4 / 5, 4, pt3);
			}
		});

	}

	public void drawRed(final Canvas canvas) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Paint pt4 = new Paint();
				pt4.setColor(getResources().getColor(R.color.moneyout));
				pt4.setStyle(Paint.Style.FILL);
				canvas.drawCircle(rect.right *4 / 5, rect.bottom * 4 / 5, 4,
						pt4);
			}
		});

	}

	private void drawBox(final Canvas canvas) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Paint pt2 = new Paint();
				if(isSelectCurrentMonth){
					pt2.setColor(getResources().getColor(
							R.color.currentmonth_biankuang));
				}else{
					pt2.setColor(getResources().getColor(
							R.color.othermonth_biankuang));
				}
				pt2.setStyle(Paint.Style.STROKE);
				canvas.drawRect(rect, pt2);
			}
		});
	}

	// 绘制日历方格
	private void drawDayView(Canvas canvas, boolean bFocused,
			boolean isShowBlue, boolean isShowRed) {
		if (bSelected || bFocused) {
			LinearGradient lGradBkg = null;

			if (bFocused) {
				lGradBkg = new LinearGradient(rect.left, 0, rect.right, 0,
						0xffCDD0DD, 0xffCDD0DD, Shader.TileMode.CLAMP);
			}

			if (bSelected) {
				lGradBkg = new LinearGradient(rect.left, 0, rect.right, 0,
						0xff8ABD3F, 0xff8ABD3F, Shader.TileMode.CLAMP);
			}
			if (lGradBkg != null) {
				pt.setShader(lGradBkg);
				canvas.drawRect(rect, pt);
			}
			pt.setShader(null);

		} else {
			pt.setColor(getResources().getColor(R.color.white));
			canvas.drawRect(rect, pt);
		}
		drawBox(canvas);
		if (isSelectCurrentMonth) {
			if (isShowBlue) {
				drawBlue(canvas);
			}
			if (isShowRed) {
				drawRed(canvas);
			}
		}
	}

	// 绘制日历中的数字
	public void drawDayNumber(Canvas canvas) {
		pt.setTypeface(null);
		pt.setAntiAlias(true);
		pt.setShader(null);
		pt.setFakeBoldText(true);
		pt.setTextSize(CalendarConstant.fTextSize_body);

		pt.setUnderlineText(false);
		if (!isSelectCurrentMonth) {
			pt.setColor(CalendarConstant.unPresentMonth_FontColor);
		} else {
			pt.setColor(CalendarConstant.isPresentMonth_FontColor);
		}
		// 是否有下划线
		if (bToday) {
			pt.setUnderlineText(false);
		} else {
			pt.setUnderlineText(false);
		}
		final int iPosX = (int) rect.left + ((int) rect.width() >> 1)
				- ((int) pt.measureText(sDate) >> 1);
		final int iPosY = (int) (this.getHeight()
				- (this.getHeight() - getTextHeight()) / 2 - pt
				.getFontMetrics().bottom);

		canvas.drawText(sDate, iPosX, iPosY, pt);

	}

	// 得到字体高度
	private int getTextHeight() {
		return (int) (-pt.ascent() + pt.descent());
	}

	// 根据条件返回不同颜色值
	public static int getColorBkg(boolean bHoliday, boolean bToday) {
		if (bToday)
			return CalendarConstant.isToday_BgColor;
		// if (bHoliday) //如需周末有特殊背景色，可去掉注释
		// return Calendar_TestActivity.isHoliday_BgColor;
		return CalendarConstant.Calendar_DayBgColor;
	}

	// 设置是否被选中
	@Override
	public void setSelected(boolean bEnable) {
		if (this.bSelected != bEnable) {
			this.bSelected = bEnable;
			this.invalidate();
		}
	}

	public void setItemClick(OnItemClick itemClick) {
		this.itemClick = itemClick;
	}

	public void doItemClick() {
		if (itemClick != null)
			itemClick.OnClick(this);
	}

	// 点击事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean bHandled = false;
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			bHandled = true;
			bTouchedDown = true;
			invalidate();
			startAlphaAnimIn(DateWidgetDayCell.this);
		}
		if (event.getAction() == MotionEvent.ACTION_CANCEL) {
			bHandled = true;
			bTouchedDown = false;
			invalidate();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			bHandled = true;
			bTouchedDown = false;
			invalidate();
			doItemClick();
		}
		return bHandled;
	}

	// 点击事件
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean bResult = super.onKeyDown(keyCode, event);
		if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
				|| (keyCode == KeyEvent.KEYCODE_ENTER)) {
			doItemClick();
		}
		return bResult;
	}

	// 不透明度渐变
	public static void startAlphaAnimIn(View view) {
		AlphaAnimation anim = new AlphaAnimation(0.5F, 1);
		anim.setDuration(CalendarConstant.ANIM_ALPHA_DURATION);
		anim.startNow();
		view.startAnimation(anim);
	}

	public void CreateReminder(Canvas canvas, int Color) {
		pt.setStyle(Paint.Style.FILL_AND_STROKE);
		pt.setColor(Color);
		Path path = new Path();
		path.moveTo(rect.right - rect.width() / 4, rect.top);
		path.lineTo(rect.right, rect.top);
		path.lineTo(rect.right, rect.top + rect.width() / 4);
		path.lineTo(rect.right - rect.width() / 4, rect.top);
		path.close();
		canvas.drawPath(path, pt);
	}
}