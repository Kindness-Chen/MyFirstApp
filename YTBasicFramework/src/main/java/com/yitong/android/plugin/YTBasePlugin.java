package com.yitong.android.plugin;

import android.text.TextUtils;

/**
 * 插件基类，项目中插件需要继承此类，防止js注入
 *
 * @author tongxu_li
 * Copyright (c) 2014 Shanghai P&C Information Technology Co., Ltd.
 */
public class YTBasePlugin {

    /**
     * 防止js注入
     */
    public Object getClass(Object o) {
        return null;
    }

    public final static String LOAD_URL_JS_BTN_TAG = "url=";
    
    /**
     * 处理js回调函数，如果函数不带括号，就加上括号
     */
    public String handleJsFunc(String func) {
        String handledFunc = func;
        if (!TextUtils.isEmpty(func)) {
            if (!func.startsWith(LOAD_URL_JS_BTN_TAG) && !func.endsWith(")")) {
                handledFunc += "()";
            }
        }
        return handledFunc;
    }
}
