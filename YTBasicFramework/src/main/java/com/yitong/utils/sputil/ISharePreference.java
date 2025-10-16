package com.yitong.utils.sputil;

/**
 * 持久化配置接口
 * Create by tongxu_li
 * Copyright (c) 2016 Shanghai P&C Information Technology Co., Ltd.
 */
public interface ISharePreference {

	boolean removeData(String key);

	boolean removeAllData();
	
	String getInfoFromShared(String key);

	String getInfoFromShared(String key, String defValue);
	
	boolean setInfoToShared(String key, String value);
	
	boolean getInfoFromShared(String key, boolean defValue);
	
	boolean setInfoToShared(String key, boolean value);
	
	int getInfoFromShared(String key, int defValue);
	
	boolean setInfoToShared(String key, int value);
}
