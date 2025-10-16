package com.yitong.nfc.pboc.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 卡片信息
 * <p>
 * Created by 左克飞 on 16/12/12 下午6:48.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.bean
 */

public class CardInfo {

    private double balance;//余额
    private String cardNo;//卡号
    private String startDate;//有效期开始
    private String endDate;//有效期结束

    private ArrayList<TransLog> transLogs;

    public CardInfo() {
        transLogs = new ArrayList<TransLog>();
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ArrayList<TransLog> getTransLogs() {
        return transLogs;
    }

    public void setTransLogs(ArrayList<TransLog> transLogs) {
        this.transLogs = transLogs;
    }

    public String toJsonStr() {
        return toJson().toString();
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("balance", balance);
            jsonObject.put("cardNo", cardNo);
            jsonObject.put("startDate", startDate);
            jsonObject.put("endDate", endDate);
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; transLogs != null && i < transLogs.size(); i++)
                jsonArray.put(transLogs.get(i).toJson());
            jsonObject.put("transLogs", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
