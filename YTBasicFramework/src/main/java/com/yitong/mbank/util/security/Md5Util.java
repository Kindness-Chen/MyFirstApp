package com.yitong.mbank.util.security;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5操作工具类（字符串、字节、文件）
 * 基于org.apache.commons.codec封装
 * @author tongxu_li
 * Copyright (c) 2016 Shanghai P&C Information Technology Co., Ltd.
 */
public class Md5Util {
	
	/**
	 * 字符串md5编码
	 * @param stringData
	 * @return
	 */
	public static String encode(final String stringData) {
		String md5Result = null;
		try {
			md5Result = encode(stringData.getBytes("utf-8"));		
		} catch (UnsupportedEncodingException e) {

		}
		return md5Result;			
	}
	
	/**
	 * 字节流md5编码
	 * @param byteData
	 * @return
	 */
	public static String encode(final byte[] byteData) {
		String md5Result = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteData);
			md5Result = HexUtil.encode(md5.digest());
			md5.reset();

		} catch (NoSuchAlgorithmException e) {
			Log.e("com.diguotech.Util.MD5", "get md5 str error!");
		}
		return md5Result;		
	}
	
	/**
	 * 获取文件md5
	 * @param filePath
	 * @return
	 */
	public static String fileMd5(final String filePath) {
		File file = new File(filePath);
		return fileMd5(file);
	}
	
	/**
	 * 获取文件md5
	 * @param file
	 * @return
	 */
	public static String fileMd5(final File file) {
		if (file == null || !file.exists() || !file.isFile()) {
			return null;
		}
		String value = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024 * 10];
            int count = 0;
            while ((count = in.read(buffer)) > 0) {
            	md5.update(buffer, 0, count);
            }
            value = HexUtil.encode(md5.digest());
            md5.reset();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	SafeCloseUtils.close(in);
        }
        return value;		
	}
}
