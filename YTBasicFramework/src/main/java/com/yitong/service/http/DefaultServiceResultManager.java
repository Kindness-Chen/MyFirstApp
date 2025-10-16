package com.yitong.service.http;

import com.yitong.service.http.APPResponseHandler.ServiceResultManager;


/**
 * 标准返回结果管理
 *
 * @author tongxu_li
 *         Copyright (c) 2014 Shanghai P&C Information Technology Co., Ltd.
 */
public class DefaultServiceResultManager implements ServiceResultManager {

    // 业务响应状态（true|false）
    private static final String KEY_STATUS = "success";

    // 业务响应信息（success＝true 为正常的操作信息，success＝false 为错误信息）
    private static final String KEY_MESSAGE = "message";

    // 业务错误状态码
    private static final String KEY_ERROR = "error";

    // 业务数据
    private static final String KEY_RESULT = "result";

    @Override
    public String getStatusKey() {
        return KEY_STATUS;
    }

    @Override
    public String getMessgaeKey() {
        return KEY_MESSAGE;
    }

    @Override
    public String getErrorCodeKey() {
        return KEY_ERROR;
    }

    @Override
    public String getResultKey() {
        return KEY_RESULT;
    }
}
