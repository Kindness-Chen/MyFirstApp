package com.yitong.consts;

/**
 * 资源初始化设置标示
 * 为了解决应用推到后台时候，静态资源被全部回收，导致应用出现异常
 * 应用在启动页时候设置初始化状态，如果在应用中此状态被重置，表示应用资源被回收了，需要推到启动页重新走流程
 *
 * @author tongxu_li
 * Copyright (c) 2016 Shanghai P&C Information Technology Co., Ltd.
 */
public class YTInitConstans {

    public static String DEVICE_ID = "";

    private static boolean gIsAppInit = false;

    public static boolean isAppInit() {
        return gIsAppInit;
    }

    public static void setAppInit(boolean is) {
        gIsAppInit = is;
    }

    //多语言参数
    public static final String LANG_TYPE_CN = "zh_CN";
    public static final String LANG_TYPE_TW = "zh_TW";
    public static final String LANG_TYPE_EN = "EN";
    public static final String LANG_TYPE_PT = "pt_PT";
    public static String CURR_LANG_TYPE = LANG_TYPE_TW;

}
