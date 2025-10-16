package com.yitong.service.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.yitong.http.AsyncHttpResponseHandler;
import com.yitong.http.ResponseHandlerInterface;
import com.yitong.http.YTSSLSocketFactory;
import com.yitong.http.cookie.YTCookieStore;
import com.yitong.safe.io.AssetFileInputStream;
import com.yitong.service.param.YTRequestParams;
import com.yitong.utils.StringTools;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

import okhttp3.Cookie;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * 基于OkHttp的异步请求
 *
 * @author tongxu_li
 * Copyright (c) 2016 Shanghai P&C Information Technology Co., Ltd.
 */
public class APPRestClient {
    private static final String LOG_TAG = "APPRestClient";
    private static final String DEFAULT_CONTENTTYPE = "application/json";
    private static final String DEFAULT_CHARSET = "utf-8";

    private static MyHttpClient client;
    private static EncryptDelegate gDelegate;

    static {
        client = createDefaultClient();
    }

    /**
     * 创建一个基本设置的异步请求对象
     * 注意：如果请求出现错误 java.io.IOException: unexpected end of stream
     * 尝试添加头部 newClient.addHeader("Connection", "close");
     * 参考：http://blog.csdn.net/zhangteng22/article/details/52233126
     */
    public static MyHttpClient createDefaultClient() {
        MyHttpClient newClient = new MyHttpClient(30000, 30000, 60000);
        newClient.addHeader("user-agent", "android");
        newClient.setCharset(DEFAULT_CHARSET);
        newClient.setLoggingEnabled(false);
//        if (BuildConfig.VERSION_TAG.equals(VersionTag.UAT)) {
//            newClient.setSSLSocketFactory(HttpsUtils.getPassSSLFactory());
//            newClient.setHostnameVerifier(new HostnameVerifier() {
//                @Override
//                public boolean verify(String hostname, SSLSession session) {
//                    return true;
//                }
//            });
//        }
        return newClient;
    }

    /**
     * 设置单证书认证
     *
     * @param context 上下文
     * @param cert    必须存放在assert目录下的证书文件
     */
    public static void setTrustSingleCertificate(Context context, String cert) {
        if (TextUtils.isEmpty(cert)) {
            throw new IllegalArgumentException("授信证书为空");
        }
        List<String> listCert = new ArrayList<String>();
        listCert.add(cert);
        setTrustCertificateList(context, listCert);
    }

    /**
     * 设置多个指定证书认证
     *
     * @param context  上下文
     * @param listCert 必须存放在assert目录下的证书文件
     */
    public static void setTrustCertificateList(Context context, List<String> listCert) {
        List<InputStream> cerInStreams = new ArrayList<InputStream>();
        if (listCert == null) {
            throw new IllegalArgumentException("授信证书列表为空");
        }
        for (String cer : listCert) {
            try {
                InputStream in = new AssetFileInputStream(context, cer);
                cerInStreams.add(in);
            } catch (Exception e) {
                throw new IllegalArgumentException("授信证书文件不存在，请检查文件名称");
            }
        }
        SSLSocketFactory sslSocketFactory = YTSSLSocketFactory.sslFactoryForCertificates(context, cerInStreams);
        if (sslSocketFactory == null) {
            throw new IllegalArgumentException("创建sslSocketFactory失败，请检查证书列表");
        }
        client.setSSLSocketFactory(sslSocketFactory);
    }

    /**
     * 获取 MyHttpClient
     */
    public static MyHttpClient getMyHttpClient() {
        return client;
    }

    /**
     * 获取客户端cookie列表
     */
    public static List<Cookie> getCookies() {
        List<Cookie> cookies = null;
        YTCookieStore cookieStore = client.getCookieStore();
        if (cookieStore != null) {
            cookies = cookieStore.getCookies();
        }
        return cookies;
    }

    /**
     * 清除客户端cookie列表
     */
    public static void clearCookies() {
        YTCookieStore cookieStore = client.getCookieStore();
        if (cookieStore != null) {
            cookieStore.clear();
        }
    }

    /**
     * 验证码下载
     *
     * @param url
     * @param callback
     */
    public static void downloadCheckCode(final String url, final CheckCodeDownLoadCallback callback) {
        post(url, "", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Headers headers, byte[] responseBody) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
                if (bitmap != null) {
                    callback.onSuccess(url, bitmap);
                } else {
                    callback.onFailure(url, -1, "验证码下载失败");
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, byte[] responseBody, Throwable error) {
                callback.onFailure(url, -1, "验证码下载失败");
            }

        }, null);
    }

    /**
     * 下载html
     *
     * @param url
     * @param callback
     */
    public static void downloadHtml(final String url, final HtmlDownLoadCallback callback) {
        post(url, "", new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Headers headers, byte[] responseBody) {
                if (responseBody != null) {
                    String html = new String(responseBody);
                    callback.onSuccess(html);
                } else {
                    callback.onFailure(url, -1, "下载失败");
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, byte[] responseBody, Throwable error) {
                callback.onFailure(url, -1, "下载失败");
            }

        }, null);
    }

    /**
     * 表单get提交方式
     *
     * @param url
     * @param params
     * @param responseHandler
     */
    public static void get(String url, YTRequestParams params, ResponseHandlerInterface responseHandler) {
        StringBuilder absoluteUrl = new StringBuilder();
        absoluteUrl.append(url);
        if (params != null) {
            String paramsString = params.getParamsString();
            if (paramsString != null && !paramsString.equals("")) {
                absoluteUrl.append("?");
                absoluteUrl.append(paramsString);
            }
        }

        client.get(absoluteUrl.toString(), responseHandler);
    }

    /**
     * 表单post提交方式
     *
     * @param url
     * @param params
     * @param responseHandler
     * @param key
     */
    public static void post(String url, YTRequestParams params, ResponseHandlerInterface responseHandler, String key) {

        String contentType = DEFAULT_CONTENTTYPE;
        String paramsString = "";
        if (params != null) {
            contentType = params.getContentType();
            paramsString = params.getParamsString();
        }

        post(url, paramsString, contentType, responseHandler, key);
    }

    /**
     * 字符串post默认提交方式
     * ContentType:application/json
     *
     * @param url
     * @param params
     * @param responseHandler
     * @param key
     */
    public static void post(String url, String params, ResponseHandlerInterface responseHandler, String key) {
        post(url, params, DEFAULT_CONTENTTYPE, responseHandler, key);
    }

    /**
     * 字符串post可设置ContentType提交方式
     *
     * @param url
     * @param params
     * @param contentType
     * @param responseHandler
     * @param key
     */
    public static void post(String url, String params, String contentType, ResponseHandlerInterface responseHandler, String key) {
        String postbody = getEncyptString(params, key);
        client.post(url, postbody, contentType, responseHandler);
    }

    /**
     * 表单同步post提交方式
     *
     * @param url
     * @param params
     * @param key
     */
    public static Response execute(String url, YTRequestParams params, String key) throws IOException {

        String contentType = DEFAULT_CONTENTTYPE;
        String paramsString = "";
        if (params != null) {
            contentType = params.getContentType();
            paramsString = params.getParamsString();
        }

        return execute(url, paramsString, contentType, key);
    }

    /**
     * 字符串同步post可设置ContentType提交方式
     *
     * @param url
     * @param params
     * @param contentType
     * @param key
     */
    private static Response execute(String url, String params, String contentType, String key) throws IOException {
        String postbody = getEncyptString(params, key);
        return client.execute(url, postbody, contentType);
    }


    /**
     * 根据加密代理规则生成字符串，无代理则不加密
     *
     * @param params
     * @param key
     */
    private static String getEncyptString(String params, String key) {
        String encyptString = "";
        if (StringTools.isEmpty(params)) {
            encyptString = "";
        } else {
            encyptString = params;
            if (gDelegate != null && !StringTools.isEmpty(key)) {
                encyptString = gDelegate.getEncryptString(params, key);
            }
        }

        return encyptString;
    }

    /**
     * 软件更新调用接口
     * 复制OkHttpClient实例，设置默认认证，防止单证书无法更新
     *
     * @param url             保存key:value的对象
     * @param params
     * @param responseHandler
     * @param key
     */
    public static void appUpdate(String url, YTRequestParams params, ResponseHandlerInterface responseHandler, String key) {

        String contentType = DEFAULT_CONTENTTYPE;
        String paramsString = "";
        if (params != null) {
            contentType = params.getContentType();
            paramsString = params.getParamsString();
        }

        appUpdate(url, paramsString, contentType, responseHandler, key);
    }

    /**
     * 软件更新调用接口
     * 复制OkHttpClient实例，设置默认认证，防止单证书无法更新
     *
     * @param url             post报文字符串
     * @param params
     * @param responseHandler
     * @param key
     */
    public static void appUpdate(String url, String params, ResponseHandlerInterface responseHandler, String key) {
        appUpdate(url, params, DEFAULT_CONTENTTYPE, responseHandler, key);
    }

    /**
     * 软件更新调用接口
     * 复制OkHttpClient实例，设置默认认证，防止单证书无法更新
     *
     * @param url             post报文字符串
     * @param params
     * @param responseHandler
     * @param key
     */
    public static void appUpdate(String url, String params, String contentType, ResponseHandlerInterface responseHandler, String key) {

        MyHttpClient updateClient = createDefaultClient();
        String postbody = getEncyptString(params, key);
        updateClient.post(url, postbody, contentType, responseHandler);
    }

    /**
     * 销毁activity使用
     */
    public static void cancelRequests(Context context) {
        client.cancelRequests();
    }

    /**
     * 设置加密代理
     *
     * @param delegate
     */
    public static void setEncryptDelegate(EncryptDelegate delegate) {
        gDelegate = delegate;
    }

    /**
     * 设置加密代理
     *
     * @author tongxu_li
     * Copyright (c) 2014 Shanghai P&C Information Technology Co., Ltd.
     */
    public interface EncryptDelegate {
        /**
         * 对明文进行加密处理
         *
         * @param decryptString 明文
         * @param key           密钥
         * @return
         */
        public String getEncryptString(String decryptString, String key);
    }

    /**
     * 验证码下载回调
     *
     * @author tongxu_li
     * Copyright (c) 2016 Shanghai P&C Information Technology Co., Ltd.
     */
    public interface CheckCodeDownLoadCallback {

        /**
         * 验证码下载成功回调
         *
         * @param url
         * @param bitmap
         */
        public void onSuccess(String url, Bitmap bitmap);

        /**
         * 验证码下载成功回调
         *
         * @param url
         * @param errorCode
         * @param errorMsg
         */
        public void onFailure(String url, int errorCode, String errorMsg);
    }


    /**
     * 條款下載回調
     */
    public interface HtmlDownLoadCallback {

        /**
         * 下载成功回调
         *
         * @param html
         */
        public void onSuccess(String html);

        /**
         * 验证码下载成功回调
         *
         * @param url
         * @param errorCode
         * @param errorMsg
         */
        public void onFailure(String url, int errorCode, String errorMsg);
    }
}