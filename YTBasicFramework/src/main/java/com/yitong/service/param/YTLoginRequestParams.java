package com.yitong.service.param;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.yitong.consts.YTInitConstans;
import com.yitong.logs.Logs;
import com.yitong.mbank.util.security.AESCoder;

/**
 * 扩展请求参数类，配合最新后台框架使用
 *
 * @author tongxu_li
 * Copyright (c) 2016 Shanghai P&C Information Technology Co., Ltd.
 */
public class YTLoginRequestParams implements YTRequestParams {

    private static String TAG = "YTLoginRequestParams";

    private Map<String, Object> headerMap;
    private Map<String, Object> payloadMap;


    /**
     * {"header":{"_t":"时间戳","service":"接口服务码"},"payload":{"key1":"value1","key2":"value2"}}
     *
     * @param strServiceCode
     */
    public YTLoginRequestParams(String strServiceCode) {
        this.headerMap = new HashMap<String, Object>();
        this.payloadMap = new HashMap<String, Object>();
        setHeaderParams(strServiceCode);
    }

    private void setHeaderParams(String strServiceCode) {
        long currTime = System.currentTimeMillis();
        headerMap.put("_t", currTime);
        headerMap.put("service", strServiceCode);
        headerMap.put("_deviceId", YTInitConstans.DEVICE_ID);
        try {
            String x = AESCoder.encrypt(String.valueOf(currTime));
            headerMap.put("_x", getMixedX(x, 3));
        } catch (Exception e) {
            headerMap.put("_x", "");
        }
    }

    //截取后字符串N位放到前面
    private String getMixedX(String x, int splitLength) {
        String startPart = x.substring(0, x.length() - splitLength);
        String endPart = x.substring(x.length() - splitLength, x.length());
        return endPart + startPart;
    }

    @Override
    public void put(String key, Object value) {
        payloadMap.put(key, value);
    }

    @Override
    public String getContentType() {
        return "application/json";
    }

    @Override
    public String getParamsString() {
        String paramsString = "";

        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("header", headerMap);
        paramsMap.put("payload", payloadMap);

        StringBuilder sb = new StringBuilder();
        Gson gson = new Gson();
        sb.append(gson.toJson(paramsMap));
        paramsString = sb.toString();

        Logs.e(TAG, paramsString);
        return paramsString;
    }
}
