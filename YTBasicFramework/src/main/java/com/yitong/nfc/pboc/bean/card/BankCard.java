package com.yitong.nfc.pboc.bean.card;

import com.yitong.nfc.ByteUtil;
import com.yitong.nfc.pboc.PbocException;
import com.yitong.nfc.pboc.tech.IsoTag;
import com.yitong.nfc.pboc.tech.ber.BerHouse;
import com.yitong.nfc.pboc.tech.ber.BerT;
import com.yitong.nfc.pboc.tech.ber.BerTLV;
import com.yitong.nfc.pboc.tech.ber.BerV;
import com.yitong.nfc.pboc.tech.ber.TagDict;
import com.yitong.nfc.pboc.tech.cmd.Command;
import com.yitong.nfc.pboc.tech.cmd.CommandFactory;
import com.yitong.nfc.pboc.tech.cmd.ExternAuth;
import com.yitong.nfc.pboc.tech.cmd.GenerateACByARQC;
import com.yitong.nfc.pboc.tech.cmd.GenerateACByTC;
import com.yitong.nfc.pboc.tech.cmd.GetData;
import com.yitong.nfc.pboc.tech.cmd.GetProcessOptions;
import com.yitong.nfc.pboc.tech.cmd.ReadRecord;
import com.yitong.nfc.pboc.tech.rsp.GetDataRsp;
import com.yitong.nfc.pboc.tech.rsp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 银行卡
 * Created by 左克飞 on 2016/11/24 13:01.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.bean.card
 */

public abstract class BankCard extends Card {
    private final String TAG = "BankCard";

    private String tag_82 = null;//aip 应用交互特征
    private String afl = null;//应用文件定位器

    public String tag_5F2D = null;
    public String tag_9F11 = null;

    /**
     * 以下标签是在pdol中获取到
     */
    private String tag_9F47 = null;
    private String tag_9F46 = null;
    private String tag_9F08 = null;
    private String tag_5F25 = null;
    private String tag_9F07 = null;
    private String tag_5F28 = null;
    private String tag_5F24 = null;
    private String tag_9F0F = null;
    private String tag_9F0E = null;
    private String tag_9F0D = null;
    private String tag_5F20 = null;
    private String tag_57 = null;
    private String tag_5A = null;
    private String tag_8E = null;
    private String tag_9F1F = null;
    private String tag_9F48 = null;
    private String tag_9F62 = null;
    private String tag_9F61 = null;
    private String tag_9F23 = null;
    private String tag_9F14 = null;
    private String tag_5F30 = null;
    private String tag_9F42 = null;
    private String tag_9F44 = null;
    private String tag_97 = null;
    private String tag_8D = null;
    private String tag_8C = null;
    private String tag_9F49 = null;
    private String tag_5F34 = null;
    private String tag_8F = null;
    private String tag_92 = null;
    private String tag_9F32 = null;
    private String tag_90 = null;
    private String tag_9F63 = null;
    private String tag_9F4A = null;
    private String tag_93 = null;

    /**
     * 通过getData方式获取数据
     */
    private String tag_9F79 = null;
    private String tag_9F78 = null;
    private String tag_9F77 = null;
    private String tag_9F13 = null;
    private String tag_9F36 = null;
    private String tag_9F51 = null;
    private String tag_9F4F = null;

    private String tag_9F27 = null;
    private String tag_9F26 = null;
    private String tag_9F10 = null;
    private String tag_9F37 = null;
    private String tag_9A = null;
    private String tag_9F21 = null;
    private String tag_9F02 = null;


    private String tag_9F03 = "000000000000"; // 其他金额
    private String tag_9F1A = "0156"; // 终端国家代码
    private String tag_5F2A = "0156";// 交易货币代码

    // 中国邮储
    private String tag_95 = "808004E000";// 终端验证结果
    private String tag_9F33 = "604020";// 终端性能
    private String tag_9F34 = "020300";// 持卡人验证方法结果 CVM 3byte
    private String tag_9F35 = "14";// 终端类型 1byte
    private String tag_9F1E = "";// 接口设备序列号 8
    private String tag_9F09 = "0014";// 应用版本号 2byte
    private String tag_9F41 = "0001";// 交易序列计数器 2~4byte
    private final String tag_9C = "60";// 交易类型（0刷卡消费，1取现，8转账，9支付，20退款，40持卡人账户转账）
    private final String tag_9F4E = "5053424300000000000000000000000000000000";// 商户名称
    private String tag_DF31 = "1000000000";//圈存失败，圈存成功 2000000000


    public BankCard(IsoTag isoTag) {
        super(isoTag);
        super.setAid(getAid());
    }

    @Override
    public void initCard() throws IOException, IllegalArgumentException, IllegalStateException {
        super.initCard();
        getProcessOptions();
        if (tag_5A != null && tag_5A.endsWith("F")) //卡号
            cardNo = tag_5A.substring(0, tag_5A.length() - 1);
        else
            cardNo = tag_5A;
        startDate = tag_5F25;//应用有效期
        endDate = tag_5F24;//应用失效期
        cardHolderIdNum = tag_9F61;//持卡人证件号码
        cardHolderIdType = tag_9F62;//持卡人证件类型
        cardHolderName = tag_5F20;//持卡人姓名
        cardVersion = tag_9F08;//卡片应用版本号

//        Log.d(TAG, "initCard: " + cardNo + " " + startDate + " " + endDate + " " + cardHolderIdNum + " " + cardHolderIdType + " " + cardVersion+ " " + cardHolderName);

        GetData getData = (GetData) CommandFactory.create(GetData.class);
        getData.setTag(TagDict.TAG_9F79);//电子现金余额
        getData.execute(isoTag);
        tag_9F79 = ((GetDataRsp) getData.getRsp()).getValue();
        getData.setTag(TagDict.TAG_9F78);//电子现金单笔上限
        getData.execute(isoTag);
        tag_9F78 = ((GetDataRsp) getData.getRsp()).getValue();
        getData.setTag(TagDict.TAG_9F77);//电子现金余额上限
        getData.execute(isoTag);
        tag_9F77 = ((GetDataRsp) getData.getRsp()).getValue();
        getData.setTag(TagDict.TAG_9F13);//联机ATC
        getData.execute(isoTag);
        tag_9F13 = ((GetDataRsp) getData.getRsp()).getValue();
        getData.setTag(TagDict.TAG_9F36);//ATC
        getData.execute(isoTag);
        tag_9F36 = ((GetDataRsp) getData.getRsp()).getValue();
        getData.setTag(TagDict.TAG_9F51);//货币代码
        getData.execute(isoTag);
        tag_9F51 = ((GetDataRsp) getData.getRsp()).getValue();
        getData.setTag(TagDict.TAG_9F4F);//日志文件格式
        getData.execute(isoTag);
        tag_9F4F = ((GetDataRsp) getData.getRsp()).getValue();

        if (tag_9F4D == null) {
            getData.setTag(TagDict.TAG_9F4D);//日志文件Id
            getData.execute(isoTag);
            tag_9F4D = ((GetDataRsp) getData.getRsp()).getValue();
        }
        if (tag_5F24 == null) {
            getData.setTag(TagDict.TAG_5F24);//失效日期
            getData.execute(isoTag);
            tag_5F24 = ((GetDataRsp) getData.getRsp()).getValue();
        }
        if (tag_5F25 == null) {
            getData.setTag(TagDict.TAG_5F25);//生效日期
            getData.execute(isoTag);
            tag_5F25 = ((GetDataRsp) getData.getRsp()).getValue();
        }
        if (tag_5A == null) {
            getData.setTag(TagDict.TAG_5A);//pan
            getData.execute(isoTag);
            tag_5A = ((GetDataRsp) getData.getRsp()).getValue();
        }

        readTransLog();

    }

//    public void genAC()


    /**
     * 拼接55域
     *
     * @return
     */
    public String get55Field() {

        Map<String, String> keys = new HashMap<String, String>();
        /**
         * 基本信息子域
         */
        keys.put("9F27", tag_9F27);// 密文信息数据 1byte
        keys.put("9F36", tag_9F36);// 应用交易计数器ATC 2byte
        keys.put("9F26", tag_9F26);// 应用密文,AC 8byte
        keys.put("9F10", tag_9F10);// 发卡行应用数据，最大32byte
        keys.put("9F37", tag_9F37);// 不可预知数，随机数 4byte
        keys.put("9A", tag_9A.substring(2));// 交易日期 3byte
        keys.put("9C", tag_9C);// 交易类型 1byte
        keys.put("9F02", tag_9F02);// 授权金额，6byte
        keys.put("5F2A", tag_5F2A);// 交易货币代码，0156 2byte
        keys.put("82", tag_82);// 应用交互特征，AIP 2byte
        keys.put("9F1A", tag_9F1A);// 终端国家代码，0156 2byte
        keys.put("9F03", tag_9F03);// 其他金额 6byte
        keys.put("9F33", tag_9F33);// 终端性能 3byte
        keys.put("95", tag_95);// 终端验证结果，TVR 5byte
        /**
         * 可选信息子域
         */
        keys.put("9F34", tag_9F34);// 持卡人验证方法结果 CVM 3byte
        keys.put("9F35", tag_9F35);// 终端类型 1byte
        keys.put("9F1E", tag_9F1E);// 接口设备序列号 8
        keys.put("84", tag_84);// 专用文件名称 5~16byte
        keys.put("9F09", tag_9F09);// 应用版本号 2byte
        keys.put("9F41", tag_9F41);// 交易序列计数器 2~4byte
        // 以上，必传
        keys.put("91", "");// 发卡行认证数据 8~16byte
        keys.put("71", "");// 发卡行脚本1
        keys.put("72", "");// 发卡行脚本2
        keys.put("DF31", tag_DF31);// 发卡行脚本结果
        keys.put("9F79", tag_9F79);//
        keys.put("9F63", tag_9F63);//
        /**
         * CUPMobile移动支付现场支付子域
         */
        keys.put("DF32", "");// 芯片序列号 8~32byte
        keys.put("DF33", "");// 过程密钥数据 8~32byte
        keys.put("DF34", "");// 磁道读取时间 7byte YYYYMMDDhhmmss
        /**
         * 脱机交易专用子域
         */
        keys.put("8A", "");// 授权响应码 2byte

        Set<Map.Entry<String, String>> sets = keys.entrySet();
        StringBuffer buffer = new StringBuffer();
        if (sets != null) {
            Iterator<Map.Entry<String, String>> iter = sets.iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> item = iter.next();
                String key = item.getKey();
                String value = item.getValue();
                if (key == null || key.equals("") || value == null || value.equals(""))
                    continue;
                BerTLV tlv = new BerTLV(new BerT(ByteUtil.hexString2Bytes(key)), new BerV(ByteUtil.hexString2Bytes(value)));
                buffer.append(tlv.toString());
            }
        }
        return buffer.toString();

//        return parseMap(keys);
    }

    /**
     * 生成ARQC
     *
     * @param money 金额（10000分）
     * @return
     */
    public String genARQC(String money) {
        StringBuffer moneyBuffer = new StringBuffer("000000000000");
        if (money.length() > 12) {// 交易金额不符合规范
            throw new PbocException("交易金额不符合规范,最大金额不能超过10亿位");
        }
        moneyBuffer.append(money);
        tag_9F02 = moneyBuffer.substring(moneyBuffer.length() - 12, moneyBuffer.length());
        if (tag_9A == null || tag_9F21 == null) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            String date = format.format(c.getTime());
            tag_9A = date.substring(0, 8);// 截取日期后6位
            tag_9F21 = date.substring(8, 14);// 截取时间6位
        }

        if (tag_9F37 == null) {
            String[] beforeShuffle = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
            List list = Arrays.asList(beforeShuffle);
            Collections.shuffle(list);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                sb.append((String) list.get(i));
            }
            tag_9F37 = sb.substring(2, 10);// 截取8个字符
        }

        StringBuffer arqc = new StringBuffer();// arqc
        arqc.append(tag_9F02);// 交易金额，12个数字，表示16进制数时，占用6个字节。精确到分，如100表示000000010000
        arqc.append(tag_9F03);// 其他金额，默认全0
        arqc.append(tag_9F1A);// 终端国家代码
        arqc.append(tag_95);// 终端验证结果
        arqc.append(tag_5F2A);// 交易货币代码
        arqc.append(tag_9A.substring(2));// 交易日期
        arqc.append(tag_9C);// 交易类型
        arqc.append(tag_9F37);// 随机数
        arqc.append(tag_9F21);// 交易时间
        arqc.append(tag_9F4E);// 商户名称
        return arqc.toString();
    }

    /**
     * 生成TC（交易证书）
     *
     * @param tag_8A
     * @return
     */
    public String genTC(String tag_8A) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(tag_8A);
        buffer.append(tag_9F02);// 交易金额，12个数字，表示16进制数时，占用6个字节。精确到分，如100表示000000010000
        buffer.append(tag_9F03);// 其他金额，默认全0
        buffer.append(tag_9F1A);// 终端国家代码
        buffer.append(tag_95);// 终端验证结果
        buffer.append(tag_5F2A);// 交易货币代码
        buffer.append(tag_9A.substring(2));// 交易日期
        buffer.append(tag_9C);// 交易类型
        buffer.append(tag_9F37);// 随机数
        buffer.append(tag_9F21);// 交易时间
        return buffer.toString();
    }


    /**
     * 解析AC字段，包含密文信息数据、AC、ATC和发卡行应用数据
     *
     * @param acData
     */
    private void parseAC(String acData) {
        tag_9F27 = acData.substring(0, 2);
        tag_9F36 = acData.substring(2, 6);
        tag_9F26 = acData.substring(6, 22);
        /**
         * 包含：长度指示符(1字节)、分散密钥索引(1字节)、密文版本号(1字节)、卡片验证结果（CVR）(4字节)、算法标识(1字节)、发卡行自定义数据(变长)
         */
        tag_9F10 = acData.substring(22);
    }

//    /**
//     * 将map中的tv数据转成tlv的16进制
//     *
//     * @param fields
//     * @return
//     */
//    private String parseMap(Map<String, String> fields) {
//        Set<Map.Entry<String, String>> sets = fields.entrySet();
//        StringBuffer buffer = new StringBuffer();
//        if (sets != null) {
//            Iterator<Map.Entry<String, String>> iter = sets.iterator();
//            while (iter.hasNext()) {
//                Map.Entry<String, String> item = iter.next();
//                String key = item.getKey();
//                String value = item.getValue();
//                if (key == null || key.equals("") || value == null || value.equals(""))
//                    continue;
//                BerTLV tlv = new BerTLV(new BerT(ByteUtil.hexString2Bytes(key)), new BerV(ByteUtil.hexString2Bytes(value)));
//                buffer.append(tlv.toString());
//            }
//        }
//        return buffer.toString();
//    }

    /**
     * 读余额
     *
     * @param refresh 区分读圈存完成之前还是读圈存完成之后的越
     * @return
     */
    @Override
    public double getBalance(boolean refresh) {
        if (!refresh)
            return super.getBalance(refresh);
        GetData getData = (GetData) CommandFactory.create(GetData.class);
        GetDataRsp getDataRsp = null;
        getData.setTag(TagDict.TAG_9F79);//电子现金余额
        try {
            getData.execute(isoTag);
            return Double.parseDouble(((GetDataRsp) getData.getRsp()).getValue()) / 100;
        } catch (IOException e) {
            return super.getBalance(refresh);
        }
    }

    /**
     * 获取处理选项
     *
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     */
    private void getProcessOptions() throws IOException, IllegalArgumentException, IllegalStateException {
        String pdol = genPDOL(tag_9F38);
        GetProcessOptions gpoCmd = (GetProcessOptions) CommandFactory.create(GetProcessOptions.class, ByteUtil.hexString2Bytes(pdol));
        gpoCmd.execute(isoTag);
        ArrayList<BerTLV> gpoTlvs = gpoCmd.parseTLV();
        if (gpoTlvs == null || gpoTlvs.size() == 0)
            return;
        tag_82 = gpoTlvs.get(0).getVal().toString().substring(0, 4);
        afl = gpoTlvs.get(0).getVal().toString().substring(4);
        ArrayList<BerTLV> pdolTlvs = readAFL(ByteUtil.hexString2Bytes(afl));
        BerTLV tlv_5F24 = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_5F24));
        if (tlv_5F24 != null)
            tag_5F24 = tlv_5F24.getVal().toString();
        BerTLV tlv_9F47 = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_9F47));
        if (tlv_9F47 != null)
            tag_9F47 = tlv_9F47.getVal().toString();
        BerTLV tlv_9F46 = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_9F46));
        if (tlv_9F46 != null)
            tag_9F46 = tlv_9F46.getVal().toString();
        BerTLV tlv_9F08 = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_9F08));
        if (tlv_9F08 != null)
            tag_9F08 = tlv_9F08.getVal().toString();
        BerTLV tlv_5F25 = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_5F25));
        if (tlv_5F25 != null)
            tag_5F25 = tlv_5F25.getVal().toString();
        BerTLV tlv_9F07 = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_9F07));
        if (tlv_9F07 != null)
            tag_9F07 = tlv_9F07.getVal().toString();
        BerTLV tlv_5F28 = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_5F28));
        if (tlv_5F28 != null)
            tag_5F28 = tlv_5F28.getVal().toString();
        BerTLV tlv_9F0F = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_9F0F));
        if (tlv_9F0F != null)
            tag_9F0F = tlv_9F0F.getVal().toString();
        BerTLV tlv_9F0E = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_9F0E));
        if (tlv_9F0E != null)
            tag_9F0E = tlv_9F0E.getVal().toString();
        BerTLV tlv_9F0D = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_9F0D));
        if (tlv_9F0D != null)
            tag_9F0D = tlv_9F0D.getVal().toString();
        BerTLV tlv_5F20 = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_5F20));
        if (tlv_5F20 != null)
            tag_5F20 = tlv_5F20.getVal().toString();
        BerTLV tlv_57 = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_57));
        if (tlv_57 != null)
            tag_57 = tlv_57.getVal().toString();
        BerTLV tlv_5A = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_5A));
        if (tlv_5A != null)
            tag_5A = tlv_5A.getVal().toString();
        BerTLV tlv_8E = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_8E));
        if (tlv_8E != null)
            tag_8E = tlv_8E.getVal().toString();
        BerTLV tlv_9F1F = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_9F1F));
        if (tlv_9F1F != null)
            tag_9F1F = tlv_9F1F.getVal().toString();
        BerTLV tlv_9F48 = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_9F48));
        if (tlv_9F48 != null)
            tag_9F48 = tlv_9F48.getVal().toString();
        BerTLV tlv_9F62 = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_9F62));
        if (tlv_9F62 != null)
            tag_9F62 = tlv_9F62.getVal().toString();
        BerTLV tlv_9F61 = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_9F61));
        if (tlv_9F61 != null)
            tag_9F61 = tlv_9F61.getVal().toString();
        BerTLV tlv_9F23 = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_9F23));
        if (tlv_9F23 != null)
            tag_9F23 = tlv_9F23.getVal().toString();
        BerTLV tlv_9F14 = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_9F14));
        if (tlv_9F14 != null)
            tag_9F14 = tlv_9F14.getVal().toString();
        BerTLV tlv_5F30 = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_5F30));
        if (tlv_5F30 != null)
            tag_5F30 = tlv_5F30.getVal().toString();
        BerTLV tlv_9F42 = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_9F42));
        if (tlv_9F42 != null)
            tag_9F42 = tlv_9F42.getVal().toString();
        BerTLV tlv_9F44 = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_9F44));
        if (tlv_9F44 != null)
            tag_9F44 = tlv_9F44.getVal().toString();
        BerTLV tlv_97 = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_97));
        if (tlv_97 != null)
            tag_97 = tlv_97.getVal().toString();
        BerTLV tlv_8D = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_8D));
        if (tlv_8D != null)
            tag_8D = tlv_8D.getVal().toString();
        BerTLV tlv_8C = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_8C));
        if (tlv_8C != null)
            tag_8C = tlv_8C.getVal().toString();
        BerTLV tlv_9F49 = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_9F49));
        if (tlv_9F49 != null)
            tag_9F49 = tlv_9F49.getVal().toString();
        BerTLV tlv_5F34 = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_5F34));
        if (tlv_5F34 != null)
            tag_5F34 = tlv_5F34.getVal().toString();
        BerTLV tlv_8F = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_8F));
        if (tlv_8F != null)
            tag_8F = tlv_8F.getVal().toString();
        BerTLV tlv_92 = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_92));
        if (tlv_92 != null)
            tag_92 = tlv_92.getVal().toString();
        BerTLV tlv_9F32 = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_9F32));
        if (tlv_9F32 != null)
            tag_9F32 = tlv_9F32.getVal().toString();
        BerTLV tlv_90 = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_90));
        if (tlv_90 != null)
            tag_90 = tlv_90.getVal().toString();
        BerTLV tlv_9F63 = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_9F63));
        if (tlv_9F63 != null)
            tag_9F63 = tlv_9F63.getVal().toString();
        BerTLV tlv_9F4A = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_9F4A));
        if (tlv_9F4A != null)
            tag_9F4A = tlv_9F4A.getVal().toString();
        BerTLV tlv_93 = BerHouse.findTlvByTag(pdolTlvs, new BerT(TagDict.TAG_93));
        if (tlv_93 != null)
            tag_93 = tlv_93.getVal().toString();

    }

    /**
     * 根据AFL(应用文件定位器)读取数据
     * <p/>
     * 每个要读取的文件在 AFL 中对应四个字节,含义如下:
     * <p/>
     * 字节1:短文件标识符 bit8-bit4 sfi,bit3-bit1 000
     * <p/>
     * 字节2:文件中要读取的第 1 个记录的记录号
     * <p/>
     * 字节3:文件中要读取的最后一个记录的记录号
     * <p/>
     * 字节4:从第1个记录开始的用于脱机数据认证的连续记录数
     *
     * @param afl
     * @return
     */
    private ArrayList<BerTLV> readAFL(byte[] afl) throws IOException {
        ArrayList<BerTLV> aflTlvs = new ArrayList<BerTLV>();
        if (afl.length % 4 != 0)
            throw new PbocException("AFL 数据长度不正确");
        ReadRecord readRecord = (ReadRecord) CommandFactory.create(ReadRecord.class);
        for (int i = 0; i < afl.length; i += 4) {
            byte[] temp = Arrays.copyOfRange(afl, i, i + 4);
            int sfi = temp[0] >> 3 & 0xff;
            readRecord.sfi(sfi);
            int s = temp[1] & 0xff;
            int e = temp[2] & 0xff;
            for (int j = s; j <= e; j++) {
                readRecord.index(j).execute(isoTag);
                aflTlvs.addAll(readRecord.parseTLV());
            }
        }
        return aflTlvs;
    }

    @Override
    public void readTransLog() throws IOException {
        super.readTransLog();

        int start, end, len;//定义日志目录起始、结束的sfi 和 日志记录长度
        if (tag_9F4D != null && tag_9F4D.length() == 4) {
            start = end = ByteUtil.hexString2Bytes(tag_9F4D)[0] & 0xFF;
            len = ByteUtil.hexString2Bytes(tag_9F4D)[1] & 0xFF;
        } else {
            start = 11;
            end = 31;
            len = 10;
        }

        if (transLog == null)
            transLog = new JSONArray();

        ReadRecord readRecord = (ReadRecord) CommandFactory.create(ReadRecord.class);
        if (tag_9F4F == null) {//日志文件格式
            //TODO 根据实际的卡，指定日志文件格式
        }
        for (int sfi = start; sfi <= end; sfi++) {
            readRecord.sfi(sfi);
            for (int i = 1; i <= len; i++) {
                readRecord.index(i).execute(isoTag);
                try {
                    transLog.put(parseLog(ByteUtil.hexString2Bytes(tag_9F4F), readRecord.getRsp().getBytes()));
                } catch (IllegalStateException e) {
                    break;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * 由于金融ic卡的 aid 是固定常量，因此禁止修改。
     *
     * @param aid
     */
    @Override
    public void setAid(String aid) {

    }

    public abstract String getAid();


    /**
     * 生成PDOL预处理指令
     *
     * @param tag_9F38
     * @return
     */
    private String genPDOL(String tag_9F38) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("8300");
        if (tag_9F38 != null) {
            ArrayList<BerTLV> tlvs = BerTLV.readPDOL(ByteUtil.hexString2Bytes(tag_9F38));
            BerHouse.printfTLV(tlvs);
            for (BerTLV tlv : tlvs) {
                switch (tlv.getTag().toInt()) {
                    case TagDict.TAG_9F66:// 终端交易属性
                        buffer.append(ByteUtil.bytes2HexString(buildPODL(tlv.getLen().toInt(), (byte) 0x48)));
                        break;
                    case TagDict.TAG_9F02:// 授权金额
                        buffer.append(ByteUtil.bytes2HexString(buildPODL(tlv.getLen().toInt())));
                        break;
                    case TagDict.TAG_9F03:// 其它金额
                        buffer.append(ByteUtil.bytes2HexString(buildPODL(tlv.getLen().toInt())));
                        break;
                    case TagDict.TAG_9F1A:// 终端国家代码
                        buffer.append(ByteUtil.bytes2HexString(buildPODL(tlv.getLen().toInt(), (byte) 0x01, (byte) 0x56)));
                        break;
                    case TagDict.TAG_95:// 终端验证结果
                        buffer.append(ByteUtil.bytes2HexString(buildPODL(tlv.getLen().toInt())));
                        break;
                    case TagDict.TAG_5F2A:// 交易货币代码
                        buffer.append(ByteUtil.bytes2HexString(buildPODL(tlv.getLen().toInt(), (byte) 0x01, (byte) 0x56)));
                        break;
                    case TagDict.TAG_9A:// 交易日期
                        buffer.append(ByteUtil.bytes2HexString(buildPODL(tlv.getLen().toInt())));
                        break;
                    case TagDict.TAG_9C:// 交易类型
                        buffer.append(ByteUtil.bytes2HexString(buildPODL(tlv.getLen().toInt())));
                        break;
                    case TagDict.TAG_9F37:// 不可预知数
                        buffer.append(ByteUtil.bytes2HexString(buildPODL(tlv.getLen().toInt())));
                        break;
                    case TagDict.TAG_DF69:// SM算法支持指示器（pboc3.0）
                        buffer.append(ByteUtil.bytes2HexString(buildPODL(tlv.getLen().toInt())));
                        break;
                    case TagDict.TAG_DF60:// CAPP交易指示位（pboc3.0）
                        buffer.append(ByteUtil.bytes2HexString(buildPODL(tlv.getLen().toInt())));
                        break;
                    default:
                        break;
                }
            }
            // 修改实际长度
            buffer.replace(2, 4, ByteUtil.toHex((byte) (buffer.length() / 2 - 2)));
        }
        return buffer.toString();
    }

    /**
     * 填充PDOL指令
     *
     * @param len
     * @param val
     * @return
     */
    private byte[] buildPODL(int len, byte... val) {
        /**
         * 取len和val长度最小值
         */
        final int min = Math.min((val != null) ? val.length : 0, len);
        /**
         * 取len和val长度最大值
         */
        final int max = Math.max((val != null) ? val.length : 0, len);
        byte[] data = new byte[max];
        int i = 0;
        while (i < min)
            data[i] = val[i++];
        return data;
    }

    @Override
    protected JSONObject getJson() {
        JSONObject json = super.getJson();
        try {
            json.put("tag_5F2D", tag_5F2D);
            json.put("tag_9F11", tag_9F11);
            json.put("tag_82", tag_82);
            json.put("tag_9F79", tag_9F79);
            json.put("tag_9F78", tag_9F78);
            json.put("tag_9F77", tag_9F77);
            json.put("tag_9F13", tag_9F13);
            json.put("tag_9F36", tag_9F36);
            json.put("tag_9F51", tag_9F51);
            json.put("tag_9F4F", tag_9F4F);

            JSONObject pdol = new JSONObject();
            pdol.put("tag_9F47", tag_9F47);
            pdol.put("tag_9F46", tag_9F46);
            pdol.put("tag_9F08", tag_9F08);
            pdol.put("tag_5F25", tag_5F25);
            pdol.put("tag_9F07", tag_9F07);
            pdol.put("tag_5F28", tag_5F28);
            pdol.put("tag_5F24", tag_5F24);
            pdol.put("tag_9F0F", tag_9F0F);
            pdol.put("tag_9F0E", tag_9F0E);
            pdol.put("tag_9F0D", tag_9F0D);
            pdol.put("tag_5F20", tag_5F20);
            pdol.put("tag_57", tag_57);
            pdol.put("tag_5A", tag_5A);
            pdol.put("tag_8E", tag_8E);
            pdol.put("tag_9F1F", tag_9F1F);
            pdol.put("tag_9F48", tag_9F48);
            pdol.put("tag_9F48", tag_9F48);
            pdol.put("tag_9F61", tag_9F61);
            pdol.put("tag_9F62", tag_9F62);
            pdol.put("tag_9F23", tag_9F23);
            pdol.put("tag_9F14", tag_9F14);
            pdol.put("tag_5F30", tag_5F30);
            pdol.put("tag_9F42", tag_9F42);
            pdol.put("tag_9F44", tag_9F44);
            pdol.put("tag_97", tag_97);
            pdol.put("tag_8D", tag_8D);
            pdol.put("tag_8C", tag_8C);
            pdol.put("tag_9F49", tag_9F49);
            pdol.put("tag_5F34", tag_5F34);
            pdol.put("tag_8F", tag_8F);
            pdol.put("tag_92", tag_92);
            pdol.put("tag_9F32", tag_9F32);
            pdol.put("tag_90", tag_90);
            pdol.put("tag_9F63", tag_9F63);
            pdol.put("tag_9F4A", tag_9F4A);
            pdol.put("tag_93", tag_93);
            json.put("pdol", pdol);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 根据待圈存的金额生成55域
     *
     * @param money
     */
    public void build55Field(String money) throws IOException {
        Command generateACByARQC = CommandFactory.create(GenerateACByARQC.class, ByteUtil.hexString2Bytes(genARQC(money)));
        generateACByARQC.execute(isoTag);
        parseAC(ByteUtil.bytes2HexString(generateACByARQC.getRsp().getBytes()));
    }

    /**
     * 根据发卡行认证数据，执行写卡操作
     *
     * @param authData 发卡行认证数据（10字节，后2字节是授权响应码）
     * @param loadInfo 写卡脚本
     */
    public void writeCard(String authData, String loadInfo) throws IOException {

        try {
            Command externAuth = CommandFactory.create(ExternAuth.class, ByteUtil.hexString2Bytes(authData));
            externAuth.execute(isoTag);
            if (!externAuth.getRsp().isOkay())
                throw new IllegalStateException("执行外部认证失败");
            Command generateACByTC = CommandFactory.create(GenerateACByTC.class, ByteUtil.hexString2Bytes(genTC(authData.substring(16))));
            generateACByTC.execute(isoTag);
            parseAC(ByteUtil.bytes2HexString(generateACByTC.getRsp().getBytes()));
            Response writeRsp = new Response(ByteUtil.hexString2Bytes(loadInfo), isoTag.transceive(ByteUtil.hexString2Bytes(loadInfo)));
            if (writeRsp.isOkay()) {//写卡成功
                tag_DF31 = "2000000000";//邮储银行需要的操作，重新生成55域
                isoTag.close();
            }
        } catch (IllegalStateException e) {
            //写卡失败处理
        }
    }
}
