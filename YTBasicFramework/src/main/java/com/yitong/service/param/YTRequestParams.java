package com.yitong.service.param;


/**
 * 请求参数接口
 * @author tongxu_li
 * Copyright (c) 2014 Shanghai P&C Information Technology Co., Ltd.
 */
public interface YTRequestParams {
	
	/**
	 * 设置请求ContentType
	 */
	String getContentType();
	
	/**
	 * 添加参数
	 * @param key
	 * @param value
	 */
    void put(String key, Object value);
    
    /**
     * 获取参数字符串
     */
	String getParamsString();
}