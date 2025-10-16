package com.yitong.nfc.pboc.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by 左克飞 on 16/12/12 下午6:48.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.bean
 */

public class TransLog {

    private String transNo;//交易序号
    private double transMoney;//交易金额
    private String transType;//交易类型
    private String transDate;//交易日期
    private String transTime;//交易时间

    private static final SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat HHmmss = new SimpleDateFormat("HHmmss");

    private static final SimpleDateFormat yyyy_MM_dd = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat HH_mm_ss = new SimpleDateFormat("HH:mm:ss");


    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public double getTransMoney() {
        return transMoney;
    }

    public void setTransMoney(double transMoney) {
        this.transMoney = transMoney;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransDate() {
        if (transDate.length() == 6)
            transDate = "20" + transDate;

        try {
            return yyyy_MM_dd.format(yyyyMMdd.parse(transDate));
        } catch (ParseException e) {
            return transDate;
        }
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransTime() {
        try {
            return HH_mm_ss.format(HHmmss.parse(transTime));
        } catch (ParseException e) {
            return transTime;
        }
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String toJsonStr() {
        return toJson().toString();
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("transNo", transNo);
            jsonObject.put("transMoney", transMoney);
            jsonObject.put("transType", transType);
            jsonObject.put("transDate", transDate);
            jsonObject.put("transTime", transTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
