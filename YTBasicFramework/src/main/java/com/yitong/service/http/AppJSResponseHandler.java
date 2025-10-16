package com.yitong.service.http;

import com.yitong.http.HttpResponseException;
import com.yitong.http.TextHttpResponseHandler;
import com.yitong.logs.Logs;
import com.yitong.utils.StringTools;

import javax.net.ssl.SSLPeerUnverifiedException;

import okhttp3.Headers;

/**
 * 为服务端js代理向服务器发送请求，数据转发
 * @author tongxu_li
 * Copyright (c) 2014 Shanghai P&C Information Technology Co., Ltd.
 */
public abstract class AppJSResponseHandler extends TextHttpResponseHandler {	
	private static final String LOG_TAG = "AppJSResponseHandler";
				
	// 如果返回接口是密文，设置解密代理
	private static APPResponseDecryptDelegate gDecryptDelegate = null;
	private String key = null; // 解密密钥
	
	public String successFunc = "";
	public String failureFunc = "";

	/**
	 * 无参构造函数
	 */
	public AppJSResponseHandler() {
		this(null);
	}
	
    /**
     * 创建一个默认编码为utf-8对象
     */
    public AppJSResponseHandler(String k) {
    	this(DEFAULT_CHARSET, k);
    }

    /**
     * 创建一个默认编码为指定编码的对象,该对象的回调结果集为指定泛型
     */
    public AppJSResponseHandler(String encoding, String k) {
    	super(encoding);
    	this.key = k;
    }
    
    /**
     * 
     * @param result
     * @param successFunc
     */
    public abstract void onSuccess(int errorCode, String result, String successFunc);

    /**
     * 
     * @param errorCode
     * @param failureFunc
     */
    public abstract void onFailure(int errorCode, String failureFunc);

	@Override
	public void onSuccess(int statusCode, Headers headers, String responseString) {
    	Logs.d(LOG_TAG, "接口返回"+responseString);    	

    	// 返回结果解密
    	if (null != key && gDecryptDelegate != null) {
    		if (!responseString.startsWith("{")) {
    			responseString = gDecryptDelegate.getDecryptString(responseString, key);
			}
    	}
		
		if(!StringTools.isEmpty(responseString)) {
			Logs.d(LOG_TAG, "接口解密返回"+responseString);
			onSuccess(statusCode, responseString, successFunc);
		} else {
			Logs.e(LOG_TAG, "接口解密返回数据失败");
			onFailure(APPResponseError.ERROR_CODE_DECRYPT_DATA, failureFunc);
		}
    }

	@Override
	public void onFailure(int statusCode, Headers headers, String responseString, Throwable throwable) {
		throwable.printStackTrace();
		if (throwable instanceof HttpResponseException) {
			HttpResponseException e = (HttpResponseException)throwable;
			onFailure(e.getStatusCode(), failureFunc);
		} else {
			if (throwable instanceof SSLPeerUnverifiedException) {
				onFailure(APPResponseError.ERROR_CODE_NO_PEER_CER, failureFunc);
			} else {
				onFailure(APPResponseError.ERROR_CODE_NET, failureFunc);
			}
		}
    }
        
    public static void setDecryptDelegate (APPResponseDecryptDelegate delegate) {
    	AppJSResponseHandler.gDecryptDelegate = delegate;
    }
}
