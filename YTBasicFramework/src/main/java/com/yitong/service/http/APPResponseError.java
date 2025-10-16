package com.yitong.service.http;

/**
 * 网络请求响应错误对照码（客户端内部定义）
 *
 * @author tongxu_li
 * Copyright (c) 2015 Shanghai P&C Information Technology Co., Ltd.
 */
public class APPResponseError {
    public static final String LOG_TAG = "APPResponseError";

    public static final String LANG_TYPE_CN = "zh_CN";
    public static final String LANG_TYPE_TW = "zh_TW";
    public static final String LANG_TYPE_EN = "EN";
    public static final String LANG_TYPE_PT = "pt_PT";

    public static String CURR_LANG_TYPE = LANG_TYPE_TW;

    // 网络连接错误
    public static final int ERROR_CODE_NET = -100;
    // 无可信证书
    public static final int ERROR_CODE_NO_PEER_CER = -901;
    // Json字符串转对象错误
    public static final int ERROR_CODE_FROM_JSON_TO_OBJECT = -800;
    // Json字符串解析错误
    public static final int ERROR_CODE_JSON_PARSE = -801;
    // 结果字段key不匹配
    public static final int ERROR_CODE_RESULT_KEY_NOT_CORRECT = -803;
    // onSuccess回调处理异常
    public static final int ERROR_CODE_ONSUCCESS_EXCEPTION = -804;
    // 解密响应数据失败
    public static final int ERROR_CODE_DECRYPT_DATA = -805;
    // 业务操作错误

    public static final int ERROR_CODE_BUSINESS_OPER = -700;
    //登录超时
    public static final int ERROR_CODE_LOGIN_TIME_OUT = 401;
    //被踢下线
    public static final int ERROR_CODE_LOGOUT_BY_OTHER = 403;
    //登录需要显示验证码
    public static final int ERROR_CODE_SHOW_CHECK_CODE_FOR_LOGIN = 407;
    //解除指纹或手势登录
    public static final int ERROR_CODE_USE_LOGIN_BY_TEXT = 408;
    //驗證短信之後到註冊提交擱置太久超過了會話超時時間
    public static final int ERROR_CODE_REGISTER_TIME_OUT = 409;
    //手机号为非睡眠用户和二级及以上注册用户,需弹窗去登录
    public static final int ERROR_CODE_PHONE_REGISTERED = 410;
    //手机号为非睡眠用户和五级及以上注册用户,需弹窗去登录
    public static final int ERROR_CODE_IBANKING_REGISTERED = 411;
    //註冊需要显示验证码
    public static final int ERROR_CODE_SHOW_CHECK_CODE_FOR_REGISTER = 412;
    //登录走网银用户首次登录流程
    public static final int ERROR_CODE_SHOW_FIRST_LOGIN_PROCESS_BY_NB = 413;
    //登录走修改密碼流程
    public static final int ERROR_CODE_SHOW_RESET_PWD_PROCESS = 421;
    //登录走设备认证流程
    public static final int ERROR_CODE_SHOW_DEVICE_AUTH_PROCESS = 433;

    /**
     * 获取客户端自定义错误信息，不包括业务操作错误信息
     *
     * @param errorCode
     * @return
     */
    public static String getCustomErrorMsg(int errorCode) {
        String errorMsg;

//        if (isServiceException(errorCode)) {
//            switch (CURR_LANG_TYPE) {
//                case LANG_TYPE_CN:
//                    errorMsg = "系统维护更新中，请稍后重试";
//                    break;
//                case LANG_TYPE_TW:
//                    errorMsg = "系統維護更新中，請稍後重試";
//                    break;
//                case LANG_TYPE_EN:
//                    errorMsg = "系統維護更新中，請稍後重試";
//                    break;
//                default:
//                    errorMsg = "系統維護更新中，請稍後重試";
//                    break;
//            }
//        } else {
            switch (CURR_LANG_TYPE) {
                case LANG_TYPE_CN:
                    errorMsg = "网络异常，请稍后再试";
                    break;
                case LANG_TYPE_TW:
                    errorMsg = "網絡異常，請稍後再試";
                    break;
                case LANG_TYPE_EN:
                    errorMsg = "Network Problems, Please try again later";
                    break;
                case LANG_TYPE_PT:
                    errorMsg = "Excepção de rede, Por favor tente novamente Mais tarde";
                    break;
                default:
                    errorMsg = "網絡異常，請稍後再試";
                    break;
            }
//        }

        return errorMsg + "[" + errorCode + "]";

//        if (errorCode == ERROR_CODE_NET) {
//            switch (CURR_LANG_TYPE) {
//                case LANG_TYPE_CN:
//                    errorMsg = "网络异常，请稍后再试";
//                    break;
//                case LANG_TYPE_TW:
//                    errorMsg = "網絡異常，請稍後再試";
//                    break;
//                case LANG_TYPE_EN:
//                    errorMsg = "Network connection failed, please try again later";
//                    break;
//                default:
//                    errorMsg = "網絡異常，請稍後再試";
//                    break;
//            }
//        } else if (errorCode == ERROR_CODE_NO_PEER_CER) {
//            switch (CURR_LANG_TYPE) {
//                case LANG_TYPE_CN:
//                    errorMsg = "缺少可信证书";
//                    break;
//                case LANG_TYPE_TW:
//                    errorMsg = "缺少可信證書";
//                    break;
//                case LANG_TYPE_EN:
//                    errorMsg = "No peer cer";
//                    break;
//                default:
//                    errorMsg = "缺少可信證書";
//                    break;
//            }
//        } else if (errorCode == ERROR_CODE_FROM_JSON_TO_OBJECT) {
//            switch (CURR_LANG_TYPE) {
//                case LANG_TYPE_CN:
//                    errorMsg = "对象转换失败";
//                    break;
//                case LANG_TYPE_TW:
//                    errorMsg = "對象轉換失敗";
//                    break;
//                case LANG_TYPE_EN:
//                    errorMsg = "Object conversion failed";
//                    break;
//                default:
//                    errorMsg = "對象轉換失敗";
//                    break;
//            }
//        } else if (errorCode == ERROR_CODE_JSON_PARSE) {
//            switch (CURR_LANG_TYPE) {
//                case LANG_TYPE_CN:
//                    errorMsg = "对象解析失败";
//                    break;
//                case LANG_TYPE_TW:
//                    errorMsg = "對象解析失敗";
//                    break;
//                case LANG_TYPE_EN:
//                    errorMsg = "Analysis of object failed";
//                    break;
//                default:
//                    errorMsg = "對象解析失敗";
//                    break;
//            }
//        } else if (errorCode == ERROR_CODE_RESULT_KEY_NOT_CORRECT) {
//            switch (CURR_LANG_TYPE) {
//                case LANG_TYPE_CN:
//                    errorMsg = "结果字段key不匹配";
//                    break;
//                case LANG_TYPE_TW:
//                    errorMsg = "結果字段key不匹配";
//                    break;
//                case LANG_TYPE_EN:
//                    errorMsg = "Result key not correct";
//                    break;
//                default:
//                    errorMsg = "結果字段key不匹配";
//                    break;
//            }
//        } else if (errorCode == ERROR_CODE_ONSUCCESS_EXCEPTION) {
//            switch (CURR_LANG_TYPE) {
//                case LANG_TYPE_CN:
//                    errorMsg = "onSuccess回调处理异常";
//                    break;
//                case LANG_TYPE_TW:
//                    errorMsg = "onSuccess回調處理異常";
//                    break;
//                case LANG_TYPE_EN:
//                    errorMsg = "onSuccess callback exception";
//                    break;
//                default:
//                    errorMsg = "onSuccess回調處理異常";
//                    break;
//            }
//        } else if (errorCode == ERROR_CODE_DECRYPT_DATA) {
//            switch (CURR_LANG_TYPE) {
//                case LANG_TYPE_CN:
//                    errorMsg = "解密响应数据失败";
//                    break;
//                case LANG_TYPE_TW:
//                    errorMsg = "解密響應數據失敗";
//                    break;
//                case LANG_TYPE_EN:
//                    errorMsg = "Decrypt data error";
//                    break;
//                default:
//                    errorMsg = "解密響應數據失敗";
//                    break;
//            }
//        } else {
//            switch (CURR_LANG_TYPE) {
//                case LANG_TYPE_CN:
//                    errorMsg = "未知错误";
//                    break;
//                case LANG_TYPE_TW:
//                    errorMsg = "未知錯誤";
//                    break;
//                case LANG_TYPE_EN:
//                    errorMsg = "Unknown error";
//                    break;
//                default:
//                    errorMsg = "未知錯誤";
//                    break;
//            }
//        }
//        return errorMsg;
    }

    /**
     * 是否服务端错误
     *
     * @param errorCode http错误码
     * @return
     */
    public static boolean isServiceException(int errorCode) {
        return 500 <= errorCode && errorCode < 600;
    }

}
