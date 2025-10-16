package com.yitong.nfc.pboc.bean.card;

import com.yitong.nfc.ByteUtil;
import com.yitong.nfc.pboc.bean.CardInfo;
import com.yitong.nfc.pboc.bean.TransLog;
import com.yitong.nfc.pboc.tech.IsoTag;
import com.yitong.nfc.pboc.tech.ber.BerHouse;
import com.yitong.nfc.pboc.tech.ber.BerL;
import com.yitong.nfc.pboc.tech.ber.BerT;
import com.yitong.nfc.pboc.tech.ber.BerTLV;
import com.yitong.nfc.pboc.tech.ber.BerV;
import com.yitong.nfc.pboc.tech.ber.TagDict;
import com.yitong.nfc.pboc.tech.cmd.Command;
import com.yitong.nfc.pboc.tech.cmd.CommandFactory;
import com.yitong.nfc.pboc.tech.cmd.SelectByName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * 各种卡片抽象类
 * Created by 左克飞 on 2016/11/24 12:51.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.bean.card
 */

public abstract class Card {

    private final String TAG = "Card";

    protected IsoTag isoTag;

    private String tag_DF61 = null;

    public String tag_84 = null;
    public String tag_88 = null;
    public String tag_50 = null;
    public String tag_87 = null;
    public String tag_9F12 = null;
    public String tag_DF4D = null;
    public String tag_9F4D = null;

    protected String tag_9F38 = null;
    protected String tag_4F;//应用id;
    protected String tag_9F08 = null;//卡片应用版本号
    protected String tag_9F0C = null;//发卡行基本数据文件

    protected String cardNo;//卡号
    protected String startDate;//应用生效日期
    protected String endDate;//应用失效日期
    protected String cardHolderIdNum;//持卡人证件号
    protected String cardHolderIdType;//持卡人证件类型
    protected String cardHolderName;//持卡人姓名
    protected String cardVersion;//卡片应用版本号
    protected JSONArray transLog;//交易日志

    private CardInfo cardInfo;

    private double balance;

    public Card(IsoTag isoTag) {
        this.isoTag = isoTag;
        cardInfo = new CardInfo();
    }

    public void setAid(String aid) {
        this.tag_4F = aid;
    }

    public void initCard() throws IOException, IllegalArgumentException, IllegalStateException {
        if (tag_4F != null) {
            Command seltAid = CommandFactory.create(SelectByName.class, ByteUtil.hexString2Bytes(tag_4F));
            seltAid.execute(isoTag);
            ArrayList<BerTLV> seltAidTlvs = seltAid.parseTLV();

            BerHouse.printfTLV(seltAidTlvs);
            BerTLV tlv_84 = BerHouse.findTlvByTag(seltAidTlvs, new BerT(TagDict.TAG_84));
            if (tlv_84 != null)
                tag_84 = tlv_84.getVal().toString();
            BerTLV tlv_9F08 = BerHouse.findTlvByTag(seltAidTlvs, new BerT(TagDict.TAG_9F08));
            if (tlv_9F08 != null)
                tag_9F08 = tlv_9F08.getVal().toString();
            BerTLV tlv_9F0C = BerHouse.findTlvByTag(seltAidTlvs, new BerT(TagDict.TAG_9F0C));
            if (tlv_9F0C != null)
                tag_9F0C = tlv_9F0C.getVal().toString();
            BerTLV tlv_9F12 = BerHouse.findTlvByTag(seltAidTlvs, new BerT(TagDict.TAG_9F12));
            if (tlv_9F12 != null)
                tag_9F12 = tlv_9F12.getVal().toString();
            BerTLV tlv_9F38 = BerHouse.findTlvByTag(seltAidTlvs, new BerT(TagDict.TAG_9F38));
            if (tlv_9F38 != null)
                tag_9F38 = tlv_9F38.getVal().toString();
            BerTLV tlv_9F4D = BerHouse.findTlvByTag(seltAidTlvs, new BerT(TagDict.TAG_9F4D));
            if (tlv_9F4D != null)
                tag_9F4D = tlv_9F4D.getVal().toString();
            BerTLV tlv_DF4D = BerHouse.findTlvByTag(seltAidTlvs, new BerT(TagDict.TAG_DF4D));
            if (tlv_DF4D != null)
                tag_DF4D = tlv_DF4D.getVal().toString();
            BerTLV tlv_DF61 = BerHouse.findTlvByTag(seltAidTlvs, new BerT(TagDict.TAG_DF61));
            if (tlv_DF61 != null)
                tag_DF61 = tlv_DF61.getVal().toString();
        }
    }

    /**
     * 读余额
     *
     * @param refresh 区分读圈存完成之前还是读圈存完成之后的余额
     * @return
     */
    public double getBalance(boolean refresh) {
        return balance;
    }

    /**
     * 将卡信息转成json格式字符串输出
     *
     * @return
     */
    public String toJsonStr() {
        return getJson().toString();
    }

    protected JSONObject getJson() {
        JSONObject json = new JSONObject();
        try {
            json.put("tag_4F", tag_4F);
            json.put("tag_50", tag_50);
            json.put("tag_84", tag_84);
            json.put("tag_87", tag_87);
            json.put("tag_88", tag_88);
            json.put("tag_9F08", tag_9F08);
            json.put("tag_9F0C", tag_9F0C);
            json.put("tag_9F12", tag_9F12);
            json.put("tag_9F38", tag_9F38);
            json.put("tag_9F4D", tag_9F4D);
            json.put("tag_DF4D", tag_DF4D);
            json.put("tag_DF61", tag_DF61);
            json.put("transLog", transLog);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


    /**
     * 读取交易记录
     * <p>
     * 不同卡片类型，交易记录字段不统一。子类方法选择性实现。
     */
    public void readTransLog() throws IOException {

    }

    public CardInfo getCardInfo() {
        cardInfo.setCardNo(cardNo);
        cardInfo.setBalance(getBalance(true));
        cardInfo.setStartDate(startDate);
        cardInfo.setEndDate(endDate);

        ArrayList<TransLog> transLogs = new ArrayList<TransLog>();
        try {
            for (int i = 0; this.transLog != null && i < this.transLog.length(); i++) {
                TransLog log = new TransLog();
                JSONObject json = this.transLog.getJSONObject(i);
                log.setTransNo(json.getString("tag_9F36"));
                String money = json.getString("tag_9F02");
                if (money.length() == 8)
                    log.setTransMoney(ByteUtil.toInt(money) / 100.0);
                else
                    log.setTransMoney(Double.parseDouble(money) / 100);
                log.setTransType(json.getString("tag_9C"));
                log.setTransDate(json.getString("tag_9A"));
                log.setTransTime(json.getString("tag_9F21"));
                transLogs.add(log);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        cardInfo.setTransLogs(transLogs);
        return cardInfo;
    }

    /**
     * 解析日志
     *
     * @param logData 日志数据
     */
    protected JSONObject parseLog(byte[] logFormat, byte[] logData) throws JSONException {
        int startFormat = 0;
        int startData = 0;
        JSONObject jsonLog = new JSONObject();

        while (startFormat < logFormat.length && startData < logData.length) {
            BerT t = new BerT();
            BerL l = new BerL();
            BerV v = new BerV();

            startFormat = t.read(logFormat, startFormat);
            startFormat = l.read(logFormat, startFormat);
            startData = v.read(logData, startData, l.toInt());

            jsonLog.put("tag_" + t.toString(), v.toString());

        }
        return jsonLog;
    }

}
