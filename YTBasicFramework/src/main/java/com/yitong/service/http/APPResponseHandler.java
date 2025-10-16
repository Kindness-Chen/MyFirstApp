package com.yitong.service.http;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.yitong.http.HttpResponseException;
import com.yitong.http.TextHttpResponseHandler;
import com.yitong.logs.Logs;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLPeerUnverifiedException;

import okhttp3.Headers;

/**
 * 处理网络请求回调，可根据指定的对象泛型，进行反射填充对象。
 *
 * @Description
 * @Author lewis(lgs@yitong.com.cn) 2014-1-17 下午1:44:43
 * @Class APPResponseHandler
 * Copyright (c) 2014 Shanghai P&C Information Technology Co.,Ltd. All rights reserved.
 */
public abstract class APPResponseHandler<T> extends TextHttpResponseHandler {
    private static final String LOG_TAG = "APPResponseHandler";

    // 指定结果集泛型
    private Class<T> classOfT;
    // 设置结果集是否为空
    private boolean isResultNull = true;
    // 返回结果管理器
    private static ServiceResultManager mServiceResultManager = new DefaultServiceResultManager();
    // 如果返回接口是密文，设置解密代理
    private static APPResponseDecryptDelegate gDecryptDelegate = null;
    // 响应数据解密秘钥
    private String key = null;
    // 响应数据是否为list
    private boolean isResultList = false;
    // 响应数据，如果响应数据是加密的，则是解密后的数据
    private String responseString;

    /**
     * 无参构造函数
     */
    public APPResponseHandler() {
        super();
        isResultNull = true;
    }

    /**
     * 创建一个默认编码为utf-8对象,该对象的回调结果集为空
     */
    public APPResponseHandler(String k) {
        super();
        this.key = k;
        isResultNull = true;
    }

    /**
     * 创建一个默认编码为utf-8对象,该对象的回调结果集为指定泛型
     */
    public APPResponseHandler(Class<T> classOfT) {
        this(classOfT, DEFAULT_CHARSET, null, false);
    }

    /**
     * 创建一个默认编码为utf-8对象,该对象的回调结果集为指定泛型
     */
    public APPResponseHandler(Class<T> classOfT, String k, boolean isResultList) {
        this(classOfT, DEFAULT_CHARSET, k, isResultList);
    }

    /**
     * 创建一个默认编码为指定编码的对象,该对象的回调结果集为指定泛型
     */
    public APPResponseHandler(Class<T> classOfT, String encoding, String k, boolean isResultList) {
        super(encoding);
        this.key = k;
        this.classOfT = classOfT;
        this.isResultList = isResultList;
        isResultNull = false;
    }

    /**
     * 请求成功回调单条数据
     *
     * @param result
     */
    public abstract void onSuccess(T result);

    /**
     * 请求成功回调列表数据
     *
     * @param result
     */
    public abstract void onSuccess(List<T> result);

    /**
     * 请求失败回调
     *
     * @param errorCode
     * @param errorMsg
     */
    public abstract void onFailure(int errorCode, String errorMsg);

    @Override
    public void onSuccess(int statusCode, Headers headers, String responseString) {
        Logs.e(LOG_TAG, "接口返回" + responseString);

        // 返回结果解密
        if (null != key && gDecryptDelegate != null) {
            responseString = gDecryptDelegate.getDecryptString(responseString, key);
            Logs.e(LOG_TAG, "接口解密返回:" + responseString);
        }

        this.responseString = responseString;

        boolean isSuccess = false;
        String message = "";
        int errorCode = statusCode;

        try {
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonParser().parse(responseString).getAsJsonObject();

            // 获取业务响应状态
            String keyStatus = mServiceResultManager.getStatusKey();
            if (jsonObject.has(keyStatus)) {
                isSuccess = jsonObject.get(keyStatus).getAsBoolean();
            }

            // 获取业务响应信息
            String keyMessage = mServiceResultManager.getMessgaeKey();
            if (jsonObject.has(keyMessage)) {
                JsonElement errorMsgJsonElement = jsonObject.get(keyMessage);
                if (errorMsgJsonElement.isJsonNull()) {
                    message = "";
                } else {
                    message = errorMsgJsonElement.getAsString();
                }
            }

            // 响应业务数据为空
            if (isResultNull) {
                if (isSuccess) {
                    sendSuccessWithSingleResult(null);
                } else {
                    errorCode = getErrorCode(jsonObject, errorCode);
                    onFailure(errorCode, message);
                }
                return;
            }

            // 获取业务数据
            String keyResult = mServiceResultManager.getResultKey();
            if (jsonObject.has(keyResult)) {
                //有result字段表示有数据
                if (isSuccess) {
                    JsonElement resultJsonElement = jsonObject.get(keyResult);
                    try {
                        if (!isResultList) {
                            T result = gson.fromJson(resultJsonElement, classOfT);
                            sendSuccessWithSingleResult(result);
                        } else {
                            List<T> result = new ArrayList<T>();
                            JsonArray arry = new JsonParser().parse(resultJsonElement.toString()).getAsJsonArray();
                            for (JsonElement jsonElement : arry) {
                                result.add(gson.fromJson(jsonElement, classOfT));
                            }
                            sendSuccessWithListResult(result);
                        }
                    } catch (JsonSyntaxException e) {
                        sendFailureWithCode(APPResponseError.ERROR_CODE_FROM_JSON_TO_OBJECT);
                    }
                } else {
                    errorCode = APPResponseError.ERROR_CODE_BUSINESS_OPER;
                    onFailure(errorCode, message);
                }
            } else {
                //无result字段表示无数据
                if (isSuccess) {
                    if (!isResultList) {
                        sendSuccessWithSingleResult(null);
                    } else {
                        sendSuccessWithListResult(new ArrayList<T>());
                    }
                } else {
                    errorCode = getErrorCode(jsonObject, errorCode);
                    onFailure(errorCode, message);
                }
            }
        } catch (OnSuccessException e) {
            throw new IllegalArgumentException(e);
        } catch (Exception e) {
            sendFailureWithCode(APPResponseError.ERROR_CODE_JSON_PARSE);
        }
    }

    @Override
    public void onFailure(int statusCode, Headers headers, String responseString, Throwable throwable) {
        Logs.e(LOG_TAG, "接口错误返回:" + responseString + "," + throwable.getMessage());
        throwable.printStackTrace();
        if (throwable instanceof HttpResponseException) {
            HttpResponseException e = (HttpResponseException) throwable;
//            onFailure(e.getStatusCode(), e.getMessage());
            sendFailureWithCode(e.getStatusCode());
        } else {
            if (throwable instanceof SSLPeerUnverifiedException) {
                sendFailureWithCode(APPResponseError.ERROR_CODE_NO_PEER_CER);
            } else {
                sendFailureWithCode(APPResponseError.ERROR_CODE_NET);
            }
        }
    }

    private void sendSuccessWithSingleResult(T result) {
        try {
            onSuccess(result);
        } catch (Exception e) {
            throw new OnSuccessException(e);
        }
    }

    private void sendSuccessWithListResult(List<T> result) {
        try {
            onSuccess(result);
        } catch (Exception e) {
            Logs.e(LOG_TAG, e.getMessage());
            throw new OnSuccessException(e);
        }
    }

    private void sendFailureWithCode(int errorCode) {
        onFailure(errorCode, APPResponseError.getCustomErrorMsg(errorCode));
    }

    public String getResponseString() {
        return this.responseString;
    }

    public static void setServiceResultManager(ServiceResultManager manager) {
        APPResponseHandler.mServiceResultManager = manager;
    }

    public static void setDecryptDelegate(APPResponseDecryptDelegate delegate) {
        APPResponseHandler.gDecryptDelegate = delegate;
    }

    private int getErrorCode(JsonObject jsonObject, int defaultCode) throws JSONException {
        // 获取业务响应错误码信息
        String error = mServiceResultManager.getErrorCodeKey();
        if (jsonObject.has(error)) {
            JsonElement errorCodeJsonElement = jsonObject.get(error);
            if (errorCodeJsonElement.isJsonNull()) {
                return APPResponseError.ERROR_CODE_BUSINESS_OPER;
            } else {
                return Integer.valueOf(errorCodeJsonElement.getAsString());
            }
        } else {
            return defaultCode;
        }
    }

    /**
     * 服务器响应字段设置
     *
     * @author tongxu_li
     *         Copyright (c) 2014 Shanghai P&C Information Technology Co., Ltd.
     */
    public interface ServiceResultManager {
        /**
         * 设置业务响应状态key
         */
        public String getStatusKey();

        /**
         * 设置业务响应信息key
         */
        public String getMessgaeKey();

        /**
         * 设置业务响应状态码key
         */
        public String getErrorCodeKey();

        /**
         * 设置业务数据key
         */
        public String getResultKey();
    }
}
