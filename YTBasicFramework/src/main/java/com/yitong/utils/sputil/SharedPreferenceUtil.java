package com.yitong.utils.sputil;

import com.yitong.android.application.YTBaseApplication;
import com.yitong.utils.sputil.configdb.DBSharedPreference;

/**
 * SharedPreference工具类
 * Create by tongxu_li
 * Copyright (c) 2016 Shanghai P&C Information Technology Co., Ltd.
 */
public class SharedPreferenceUtil {
    private static final String TAG = "SharedPreferenceUtil";
    private static ISharePreference iSharePreference;

    static {
        iSharePreference = new DBSharedPreference(YTBaseApplication.getInstance());
    }

    public static boolean removeData(String key) {
        return iSharePreference.removeData(key);
    }

    public static boolean removeAllData() {
        return iSharePreference.removeAllData();
    }

    public static String getInfoFromShared(String key) {
        return iSharePreference.getInfoFromShared(key);
    }

    public static String getInfoFromShared(String key, String defValue) {
        return iSharePreference.getInfoFromShared(key, defValue);
    }

    public static boolean setInfoToShared(String key, String value) {
        return iSharePreference.setInfoToShared(key, value);
    }

    public static boolean getInfoFromShared(String key, boolean defValue) {
        return iSharePreference.getInfoFromShared(key, defValue);
    }

    public static boolean setInfoToShared(String key, boolean value) {
        return iSharePreference.setInfoToShared(key, value);
    }

    public static int getInfoFromShared(String key, int defValue) {
        return iSharePreference.getInfoFromShared(key, defValue);
    }

    public static boolean setInfoToShared(String key, int value) {
        return iSharePreference.setInfoToShared(key, value);
    }

    /**
     * 判断是否首次运行
     *
     * @return
     */
    public static boolean isFirstRun() {
        return getInfoFromShared("FIRST_RUN", true);
    }

    public static void setFirstRun(boolean bool) {
        setInfoToShared("FIRST_RUN", bool);
    }

    /**
     * 获取多语言类型
     *
     * @return
     */
    public static String getLangType() {
        return getInfoFromShared("LANG_TYPE", "zh_TW");
    }

    public static void setLangType(String type) {
        setInfoToShared("LANG_TYPE", type);
    }

    /**
     * 获取上次App版本版本
     *
     * @return
     */
    public static String getLastAppVersion() {
        return getInfoFromShared("LAST_APP_VERSION", "");
    }

    public static void setLastAppVersion(String version) {
        setInfoToShared("LAST_APP_VERSION", version);
    }


    /**
     * 获取广告页图片版本
     *
     * @return
     */
    public static String getAdVersion() {
        return getInfoFromShared("AD_VERSION", "1");
    }

    public static void setAdVersion(String adVer) {
        setInfoToShared("AD_VERSION", adVer);
    }

    /**
     * 获取是否记住账户
     *
     * @return
     */
    public static boolean isRememberAcc() {
        return getInfoFromShared("REMEMBER_ACCOUNT", false);
    }

    public static void setRememberAcc(boolean bool) {
        setInfoToShared("REMEMBER_ACCOUNT", bool);
    }


    /**
     * 获取用户名
     *
     * @return
     */
    public static String getAccount() {
        return getInfoFromShared("ACCOUNT", "");
    }

    public static void setAccount(String account) {
        setInfoToShared("ACCOUNT", account);
    }

    /**
     * 获取手势密码开关
     *
     * @return
     */
    public static boolean isGestureOpen() {
        return getInfoFromShared("SWITCH_GESTURE", false);
    }

    public static void setGestureOpen(boolean open) {
        setInfoToShared("SWITCH_GESTURE", open);
    }

    /**
     * 获取手势轨迹开关
     *
     * @return
     */
    public static boolean isGesturePathOpen() {
        return getInfoFromShared("SWITCH_GESTURE_PATH", true);
    }

    public static void setGesturePathOpen(boolean open) {
        setInfoToShared("SWITCH_GESTURE_PATH", open);
    }

    /**
     * 获取最后一条公告记录ID
     *
     * @return
     */
    public static String getLastAnncId() {
        return getInfoFromShared("LAST_ANNC_ID", "0");
    }

    public static void setLastAnncId(String anncId) {
        setInfoToShared("LAST_ANNC_ID", anncId);
    }

    /**
     * 获取服务地址IP列表
     *
     * @return
     */
    public static String getIpListJson() {
        return getInfoFromShared("IP_LIST_JSON", "");
    }

    public static void setIpListJson(String json) {
        setInfoToShared("IP_LIST_JSON", json);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public static String getUserInfo() {
        return getInfoFromShared("USER_INFO", "");
    }

    public static void setUserInfo(String json) {
        setInfoToShared("USER_INFO", json);
    }


    /**
     * 判断是否同場所彈风险界面
     *
     * @return
     */
    public static boolean isDoubtful() {
        return getInfoFromShared("DOUBTFUL", false);
    }

    public static void setDoubtful(boolean open) {
        setInfoToShared("DOUBTFUL", open);
    }

    /**
     * 判断是否同場所
     *
     * @return
     */
    public static boolean isSamePlace() {
        return getInfoFromShared("SAME_PLACE", false);
    }

    public static void setSamePlace(boolean  samePlace) {
        setInfoToShared("SAME_PLACE", samePlace);
    }

    /**
     * 保存扫码进场地址的编号
     *
     * @return
     */
    public static String getLocationNo() {
        return getInfoFromShared("LOCATION_NO", "");
    }

    public static void setLocationNo(String json) {
        setInfoToShared("LOCATION_NO", json);
    }

    /**
     * 最近更新时间
     *
     * @return
     */
    public static String getLastTime() {
        return getInfoFromShared("LAST_TIME", "");
    }

    public static void setLastTime(String lastTime) {
        setInfoToShared("LAST_TIME", lastTime);
    }



    /**
     * 获取出入场记录的ID
     *
     * @return
     */
    public static String getRecordId() {
        return getInfoFromShared("RECORD_ID", "");
    }

    public static void setRecordId(String json) {
        setInfoToShared("RECORD_ID", json);
    }

    /**
     * 判斷是否離開
     *
     * @return
     */
    public static boolean isLeave() {
        return getInfoFromShared("LEAVE_PLACE", true);
    }

    public static void setLeave(boolean isLeave) {
        setInfoToShared("LEAVE_PLACE", isLeave);
    }

    /**
     * 判斷是否可以下载风险场所
     *
     * @return
     */
    public static String getRiskPlace() {
        return getInfoFromShared("RISK_PLACE", "");
    }

    public static void setRiskPlace(String riskPlace) {
        setInfoToShared("RISK_PLACE", riskPlace);
    }

    /**
     * 保存有风险状态是发通知的时间
     *
     * @return
     */
    public static String getMatchedTime() {
        return getInfoFromShared("MATCHED_TIME", "");
    }

    public static void setMatchedTime(String matchedTime) {
        setInfoToShared("MATCHED_TIME", matchedTime);
    }

    /**
     * 保存上次的反扫时间
     *
     * @return
     */
    public static String getLastScanTime() {
        return getInfoFromShared("LAST_SCAN_TIME", "");
    }

    public static void setLastScanTime(String lastScanTime) {
        setInfoToShared("LAST_SCAN_TIME", lastScanTime);
    }

}
