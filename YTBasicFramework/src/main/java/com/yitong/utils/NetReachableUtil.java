package com.yitong.utils;

import com.yitong.logs.Logs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

/**
 * 检测网络是否可以连接工具类   
 * @author tongxu_li
 * Copyright (c) 2014 Shanghai P&C Information Technology Co., Ltd.
 */
public class NetReachableUtil {
	private static String TAG = "NetReachableUtil";
	
	private static ConnectivityManager getConnectMgr(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return connManager;
	}
	
	/**
	 * 判断网络是否连接
	 * @param context
	 * @return
	 */
	public static boolean isReachable(Context context) {
		ConnectivityManager connManager = getConnectMgr(context);
		
		// 获取代表联网状态的NetWorkInfo对象
		NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return false;
		}
		
		boolean available = networkInfo.isAvailable();
		if (available) {
			Logs.i(TAG, "当前的网络连接可用");
		} else {
			Logs.i(TAG, "当前的网络连接不可用");
		}
		return available;
	}
	 
	/**
	 * 判断是否连接2G/3G网
	 * @param context
	 * @return
	 */
	public static boolean isReachableViaWWAN(Context context) {
		ConnectivityManager connManager = getConnectMgr(context);
		
		NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (networkInfo == null) {
			return false;
		}
		if (networkInfo.getState() != State.CONNECTED) {
			return false;
		}
		return true;
	}
	
	/**
	 * 判断是否连接WiFi
	 * @param context
	 * @return
	 */
	public static boolean isReachableViaWiFi(Context context) {
		ConnectivityManager connManager = getConnectMgr(context);
		
		NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo == null) {
			return false;
		}
		if (networkInfo.getState() != State.CONNECTED) {
			return false;
		}
		return true;		
	}
}
