package com.yitong.service.http;

/**
 * 响应解密代理
 * @author tongxu_li
 * Copyright (c) 2014 Shanghai P&C Information Technology Co., Ltd.
 */
public interface APPResponseDecryptDelegate {
	/**
	 * 对获取的密文进行处理
	 * @param encryptString
	 */
	public String getDecryptString(String encryptString, String key);
}
