package com.yitong.service;

import java.io.File;

/**
 * 服务地址管理类，程序启动时进行设置
 *
 * @author tongxu_li
 * Copyright (c) 2016 Shanghai P&C Information Technology Co., Ltd.
 */
public class ServiceUrlManager {

    private static String BASE_URL = "";

    private static String MICRO_SERVER_NAME = "";

    // 新版请求地址入口是一个地址
    private static String REQUEST_ADDRESS = "";

    private static String SOCKET_IP = "";

    private static int SOCKET_PORT = 9999;

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static void setBaseUrl(String url) {
        String baseUrl = url;
        if (baseUrl.endsWith("/")) {
            baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
        }
        BASE_URL = baseUrl;
    }

    public static String getDefaultMicroServerName() {
        return MICRO_SERVER_NAME;
    }

    public static void setDefaultMicroServerName(String microServerName) {
        MICRO_SERVER_NAME = microServerName;
    }

    public static String getDefaultServiceBaseUrl() {
        return getServiceBaseUrl(getDefaultMicroServerName());
    }

    public static String getDefaultResourceBaseUrl() {
        return getResourceBaseUrl(getDefaultMicroServerName());
    }

    public static String getServiceBaseUrl(String microName) {
        return getBaseUrl() + microName;
    }

    public static String getResourceBaseUrl(String microName) {
        return getBaseUrl() + microName + File.separator + "resource";
    }

    public static String getRequestAddress() {
        return REQUEST_ADDRESS;
    }

    public static void setRequestAddress(String address) {
        REQUEST_ADDRESS = address;
    }

    /**
     * 获取接口请求绝对路径
     *
     * @param relativeUrl
     * @return
     */
    public static String getDefaultServiceAbsUrl(String relativeUrl) {
        String absoluteUrl;
        if (relativeUrl.startsWith("/")) {
            absoluteUrl = getDefaultServiceBaseUrl() + relativeUrl;
        } else {
            absoluteUrl = getDefaultServiceBaseUrl() + File.separator + relativeUrl;
        }
        return absoluteUrl;
    }

    /**
     * 获取资源请求绝对路径
     *
     * @param relativeUrl
     * @return
     */
    public static String getDefaultResourceAbsUrl(String relativeUrl) {
        if (relativeUrl == null) {
            relativeUrl = "";
        }
        String absoluteUrl;
        if (relativeUrl.startsWith("/")) {
            absoluteUrl = getDefaultResourceBaseUrl() + relativeUrl;
        } else {
            absoluteUrl = getDefaultResourceBaseUrl() + File.separator + relativeUrl;
        }
        return absoluteUrl;
    }

    /**
     * 获取接口请求绝对路径
     *
     * @param microServerName
     * @param relativeUrl
     * @return
     */
    public static String getServiceAbsUrl(String microServerName, String relativeUrl) {
        if (!microServerName.startsWith("/")) {
            microServerName = "/" + microServerName;
        }
        String absoluteUrl;
        if (relativeUrl.startsWith("/")) {
            absoluteUrl = getServiceBaseUrl(microServerName) + relativeUrl;
        } else {
            absoluteUrl = getServiceBaseUrl(microServerName) + File.separator + relativeUrl;
        }
        return absoluteUrl;
    }

    /**
     * 获取资源请求绝对路径
     *
     * @param microServerName
     * @param relativeUrl
     * @return
     */
    public static String getResourceAbsUrl(String microServerName, String relativeUrl) {
        if (relativeUrl == null) {
            relativeUrl = "";
        }
        String absoluteUrl;
        if (relativeUrl.startsWith("/")) {
            absoluteUrl = getResourceBaseUrl(microServerName) + relativeUrl;
        } else {
            absoluteUrl = getResourceBaseUrl(microServerName) + File.separator + relativeUrl;
        }
        return absoluteUrl;
    }

    /**
     * 获取请求的统一入口地址（新版后台使用）
     *
     * @return
     */
    public static String getDefaultRequestAbsUrl() {
        return getDefaultServiceAbsUrl(getRequestAddress());
    }

    /**
     * 获取请求的统一入口地址（新版后台使用）
     *
     * @return
     */
    public static String getRequestAbsUrl(String microServerName) {
        return getServiceAbsUrl(microServerName, getRequestAddress());
    }

    /**
     * 获取web缓存资源地址
     *
     * @param relativeUrl
     * @return
     */
    public static String getWebCacheUrl(String relativeUrl) {
        if (relativeUrl.startsWith("#")) {
            relativeUrl = relativeUrl.substring(1);
            return getBaseUrl() + relativeUrl;
        } else {
            return getDefaultServiceAbsUrl(relativeUrl);
        }
    }

    public static String getSocketIp() {
        return SOCKET_IP;
    }

    public static void setSocketIp(String socketIp) {
        SOCKET_IP = socketIp;
    }

    public static int getSocketPort() {
        return SOCKET_PORT;
    }

    public static void setSocketPort(int socketPort) {
        SOCKET_PORT = socketPort;
    }
}
