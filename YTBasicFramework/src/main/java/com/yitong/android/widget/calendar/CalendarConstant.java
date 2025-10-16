/*
 * 作者		孙靖
 * 开发环境	Win7 Eclipse4.2 JDK1.6
 * 开发日期	2014-3-10
 */
package com.yitong.android.widget.calendar;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.util.Calendar;

public class CalendarConstant {

	// 日历头部字体大小
	public final static int fTextSize_header = 24;
	// 日历主体字体大小
	public static final int fTextSize_body = 28;

	public static int ANIM_ALPHA_DURATION = 100;

	public static final int iFirstDayOfWeek = Calendar.MONDAY;// 日历是以星期几开始的

	public static int Calendar_Width = 0;// 日历宽度
	public static int Cell_Width = 0;// 日历主题方格的宽度

	public static long currentTimeMillis = 0;// 当前日期毫秒数

	public static int Calendar_WeekBgColor = 0;
	public static int Calendar_DayBgColor = 0;
	public static int isHoliday_BgColor = 0;
	public static int unPresentMonth_FontColor = 0;
	public static int isPresentMonth_FontColor = 0;
	public static int isToday_BgColor = 0;
	public static int special_Reminder = 0;
	public static int common_Reminder = 0;
	public static int Calendar_WeekFontColor = 0;
	
	public static final int MAX_NUM_CLICK = 11; // 最大左右按键的点击次数
	public static int rightAndleftButtonClick = 0;// 左右按键的点击次数

	/**
	 * 計算指定年月的日期數
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int maxDayOfMonth(int year, int month) {
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 31;
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		case 2:
			boolean isRunYear = (year % 400 == 0)
					|| (year % 4 == 0 && year % 100 != 0);
			return isRunYear ? 29 : 28;
		default:
			return 31;
		}
	}

	/**
	 * 获取指定年月的日期數
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int maxDayOfMonth(String year, String month) {
		return maxDayOfMonth(Integer.parseInt(year), Integer.parseInt(month));
	}

	// 得到当天在日历中的序号
	public static int GetNumFromDate(Calendar now, Calendar returnDate) {
		Calendar cNow = (Calendar) now.clone();
		Calendar cReturnDate = (Calendar) returnDate.clone();
		setTimeToMidnight(cNow);
		setTimeToMidnight(cReturnDate);
		long todayMs = cNow.getTimeInMillis();
		long returnMs = cReturnDate.getTimeInMillis();
		long intervalMs = todayMs - returnMs;
		int index = millisecondsToDays(intervalMs);
		return index;
	}

	public static int millisecondsToDays(long intervalMs) {
		return Math.round((intervalMs / (1000 * 86400)));
	}

	public static void setTimeToMidnight(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	/******
	 * // 生成布局
	 * 
	 * @Description
	 * @param iOrientation
	 * @return
	 * @author 孙靖
	 * @version 1.0 2014-3-28
	 */
	public static LinearLayout createLayout(int iOrientation, Activity activity) {
		LinearLayout lay = new LinearLayout(activity);
		lay.setLayoutParams(new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		lay.setOrientation(iOrientation);
		return lay;
	}

	/*****
	 * // 得到当前日历中的第一天
	 * 
	 * @Description
	 * @return
	 * @author 孙靖
	 * @version 1.0 2014-3-28
	 */
	public static Calendar GetStartDate() {
		int iDay = 0;
		Calendar cal_Now = Calendar.getInstance();
		cal_Now.set(Calendar.DAY_OF_MONTH, 1);
		cal_Now.set(Calendar.HOUR_OF_DAY, 0);
		cal_Now.set(Calendar.MINUTE, 0);
		cal_Now.set(Calendar.SECOND, 0);
		cal_Now.setFirstDayOfWeek(Calendar.MONDAY);

		iDay = cal_Now.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;

		if (iDay < 0) {
			iDay = 6;
		}
		cal_Now.add(Calendar.DAY_OF_WEEK, -iDay);
		return cal_Now;
	}

	/******
	 * // 得到当前日历中的最后一天
	 * 
	 * @Description
	 * @param startDate
	 * @return
	 * @author 孙靖
	 * @version 1.0 2014-3-28
	 */
	public static Calendar GetEndDate(Calendar startDate) {
		// Calendar end = GetStartDate(enddate);
		Calendar endDate = Calendar.getInstance();
		endDate = (Calendar) startDate.clone();
		endDate.add(Calendar.DAY_OF_MONTH, 41);
		return endDate;
	}

	/*****
	 * 获取今天
	 * 
	 * @Description
	 * @return
	 * @author 孙靖
	 * @version 1.0 2014-3-28
	 */
	public static Calendar GetTodayDate() {
		Calendar cal_Today = Calendar.getInstance();
		cal_Today.set(Calendar.HOUR_OF_DAY, 0);
		cal_Today.set(Calendar.MINUTE, 0);
		cal_Today.set(Calendar.SECOND, 0);
		cal_Today.setFirstDayOfWeek(Calendar.MONDAY);

		return cal_Today;
	}

	
	/*****
	 * // 取变量值
	 * @Description 
	 * @param iDateYear
	 * @param iDateMonth
	 * @param iDateDay
	 * @return
	 * @author 孙靖
	 * @version 1.0 2014-3-28
	 */
	public static Calendar getDate(int iDateYear, int iDateMonth, int iDateDay) {
		Calendar calDate = Calendar.getInstance();
		calDate.clear();
		calDate.set(Calendar.YEAR, iDateYear);
		calDate.set(Calendar.MONTH, iDateMonth);
		calDate.set(Calendar.DAY_OF_MONTH, iDateDay);
		return calDate;
	}

}