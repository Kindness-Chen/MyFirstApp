package com.yitong.utils.sputil.xml;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.yitong.utils.sputil.ISharePreference;

/**
 * 系统SharedPreference存储工具
 * Create by tongxu_li
 * Copyright (c) 2016 Shanghai P&C Information Technology Co., Ltd.
 */
public class XMLSharedPreference implements ISharePreference {
    public final static String SP_NAME = "SP_NAME_INFO";

    private SharedPreferences preferences;

    public XMLSharedPreference(Context context) {
        this(context, SP_NAME);
    }

    public XMLSharedPreference(Context context, String shareName) {
        preferences = context.getSharedPreferences(shareName, Context.MODE_PRIVATE);
    }
	
	public boolean removeData(String key) {
		preferences.edit().remove(key).commit();
		return true;
	}

	public boolean removeAllData() {
		preferences.edit().clear().commit();
		return true;
	}
	
	public String getInfoFromShared(String key) {
		return preferences.getString(key, null);
	}

	public String getInfoFromShared(String key, String defValue) {
		return preferences.getString(key, defValue);
	}
	
	public boolean setInfoToShared(String key, String value) {
		Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
		return true;
	}
	
	public boolean getInfoFromShared(String key, boolean defValue) {
		return preferences.getBoolean(key, defValue);
	}
	
	public boolean setInfoToShared(String key, boolean value) {
		Editor editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
		return true;
	}
	
	public int getInfoFromShared(String key, int defValue) {
		return preferences.getInt(key, defValue);
	}
	
	public boolean setInfoToShared(String key, int value) {
		Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
		return true;
	}
}
