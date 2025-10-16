package com.yitong.mbank.util.security;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 * Hex操作工具类
 * 基于org.apache.commons.codec封装
 * @author tongxu_li
 * Copyright (c) 2016 Shanghai P&C Information Technology Co., Ltd.
 */
public class HexUtil {
	
	/**
	 * 原字节流转换成十六进制字符串
	 * @param byteData
	 * @return
	 */
	public static String encode(final byte[] byteData) {
		return new String(new Hex().encode(byteData));
	}
	
	/**
	 * 十六进制字符串转原字节流
	 * @param hexStringData
	 * @return
	 */
	public static byte[] decode(final String hexStringData) {
		return decode(hexStringData.getBytes());
	}
	
	/**
	 * 十六进制字符串字节流转换原字节流
	 * @param hexByteData
	 * @return
	 */
	public static byte[] decode(final byte[] hexByteData) {
		byte[] data = null;
		try {
			data = new Hex().decode(hexByteData);
		} catch (DecoderException e) {
			e.printStackTrace();
		}
		return data;
	}
}
