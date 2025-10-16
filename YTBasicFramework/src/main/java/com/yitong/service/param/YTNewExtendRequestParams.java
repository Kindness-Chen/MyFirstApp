package com.yitong.service.param;

import com.google.gson.Gson;
import com.yitong.consts.YTInitConstans;
import com.yitong.logs.Logs;
import com.yitong.mbank.util.security.AESCoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jeremy on 2017/6/2.
 */

public class YTNewExtendRequestParams implements YTRequestParams {

    private static String TAG = "YTExtendRequestParams";

    private Map<String, Object> headerMap;
    private Map<String, Object> payloadMap;

    /**
     * {"payload":{},"header":{"_t":1600057033676,"service":"registerService/executeBakeUp","options":[],"staticData":false}}
     *
     * @param _code
     */
    public YTNewExtendRequestParams(String _code) {
        this.headerMap = new HashMap<>();
        this.payloadMap = new HashMap<>();
        setHeaderParams(_code, 0, 10);
    }

    /**
     * 在header中添加一個新的參數authorization
     * {"payload":{},"header":{"_t":1600057033676,"service":"registerService/executeBakeUp","options":[],"staticData":false,"authorization":"authorization"}}
     *
     * @param _code
     *  @param authorization
     */
    public YTNewExtendRequestParams(String _code, String authorization) {
        this.headerMap = new HashMap<>();
        this.payloadMap = new HashMap<>();
        setHeaderParams(_code, authorization,0, 10);
    }


    private void setHeaderParams(String _code, int startIndex, int pageSizeLimit) {
        long currTime = System.currentTimeMillis();
        headerMap.put("_t", currTime);
        headerMap.put("service", _code);
        headerMap.put("staticData", false);
//        headerMap.put("options", new ArrayList());
    }

    private void setHeaderParams(String _code, String authorization, int startIndex, int pageSizeLimit) {
        long currTime = System.currentTimeMillis();
        headerMap.put("_t", currTime);
        headerMap.put("service", _code);
        headerMap.put("staticData", false);
//        headerMap.put("options", new ArrayList());
        headerMap.put("Authorization",authorization);
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

        Map<String, Object> paramsMap = new HashMap<>();
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
