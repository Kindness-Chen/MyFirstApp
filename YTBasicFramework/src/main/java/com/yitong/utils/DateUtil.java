package com.yitong.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	/**
	 * 接受yyyy年MM月dd日的日期字符串参数,返回两个日期相差的天数
	 * @param start
	 * @param end
	 * @return
	 * @throws ParseException
	 */
	public static long getDistDates(String start, String end){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		Date startDate;
		Date endDate;
		try {
			startDate = sdf.parse(start);
			endDate = sdf.parse(end);
			return getDistDates(startDate, endDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 返回两个日期相差的天数
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws ParseException
	 */
	public static long getDistDates(Date startDate, Date endDate) {
		long totalDate = 0;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startDate);
		long timestart = calendar.getTimeInMillis();
		calendar.setTime(endDate);
		long timeend = calendar.getTimeInMillis();
		totalDate = Math.abs((timeend - timestart)) / (1000 * 60 * 60 * 24);
		return totalDate;
	}
	/**
	 * 接受yyyy年MM月dd日的日期字符串参数
	 * @param start
	 * @param end
	 * @return
	 * @throws ParseException
	 */
	public static int compareTo(String start, String end){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
		Date startDate;
		Date endDate;
		try {
			startDate = sdf.parse(start);
			endDate = sdf.parse(end);
			return startDate.compareTo(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * 获得当天零时零分零秒
	 */
	public static Date getTodayZero(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}
}
