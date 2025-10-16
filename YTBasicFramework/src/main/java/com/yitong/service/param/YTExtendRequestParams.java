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

public class YTExtendRequestParams implements YTRequestParams {

    private static String TAG = "YTExtendRequestParams";

    private Map<String, Object> headerMap;
    private Map<String, Object> payloadMap;

    /**
     * {"header":{"_code":"SPI/ADAPTER-HTTP","method":"execute","service":"httpAdapterService","_t":"1496368376383","_deviceId":"xxxxxx"},"payload":{}}
     *
     * @param _code
     */
    public YTExtendRequestParams(String _code) {
        this.headerMap = new HashMap<>();
        this.payloadMap = new HashMap<>();
        setHeaderParams(_code, 0, 10);
    }

    /**
     * {"header":{"service":"SPI/ADAPTER-HTTP","method":"execute","staticData":false,"options":[{"_options":{"allowCount":true,"start":0,"limit":5},"_code":"projectInformService/queryNotice"}],"_t":"1496752204726","_deviceId":"xxxxxx"}}
     *
     * @param _code
     * @param startIndex
     */
    public YTExtendRequestParams(String _code, int startIndex, int pageSizeLimit) {
        this.headerMap = new HashMap<>();
        this.payloadMap = new HashMap<>();
        setHeaderParams(_code, startIndex, pageSizeLimit);
    }

    private void setHeaderParams(String _code, int startIndex, int pageSizeLimit) {
        long currTime = System.currentTimeMillis();
        headerMap.put("_t", currTime);
        headerMap.put("service", "SPI/ADAPTER-HTTP");
        headerMap.put("method", "execute");
        headerMap.put("_deviceId", YTInitConstans.DEVICE_ID);
        try {
            String x = AESCoder.encrypt(String.valueOf(currTime));
            headerMap.put("_x", getMixedX(x, 3));
        } catch (Exception e) {
            headerMap.put("_x", "");
        }
        List<Option> options = new ArrayList<>();
        Option option = new Option(_code, startIndex, pageSizeLimit);
        options.add(option);
        headerMap.put("options", options);
    }

    //截取后字符串N位放到前面
    private String getMixedX(String x, int splitLength) {
        String startPart = x.substring(0, x.length() - splitLength);
        String endPart = x.substring(x.length() - splitLength, x.length());
        return endPart + startPart;
    }

    private class Option {
        private _option _options;
        private String _code;

        Option(String _code, int startIndex, int pageSizeLimit) {
            _options = new _option(startIndex, pageSizeLimit);
            this._code = _code;
        }
    }

    private class _option {
        private boolean allowCount;
        private int start;
        private int limit;

        _option(int start, int limit) {
            allowCount = true;
            this.start = start;
            this.limit = limit;
        }
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
