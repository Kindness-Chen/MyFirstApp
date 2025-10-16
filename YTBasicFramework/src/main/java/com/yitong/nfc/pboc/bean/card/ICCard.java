package com.yitong.nfc.pboc.bean.card;

import android.util.Log;

import com.yitong.nfc.ByteUtil;
import com.yitong.nfc.pboc.Constants;
import com.yitong.nfc.pboc.tech.IsoTag;
import com.yitong.nfc.pboc.tech.ber.BerHouse;
import com.yitong.nfc.pboc.tech.ber.BerT;
import com.yitong.nfc.pboc.tech.ber.BerTLV;
import com.yitong.nfc.pboc.tech.ber.TagDict;
import com.yitong.nfc.pboc.tech.cmd.CommandFactory;
import com.yitong.nfc.pboc.tech.cmd.CreditForLoad;
import com.yitong.nfc.pboc.tech.cmd.GetBalance;
import com.yitong.nfc.pboc.tech.cmd.GetTransactionProve;
import com.yitong.nfc.pboc.tech.cmd.InitializeForLoad;
import com.yitong.nfc.pboc.tech.cmd.ReadBinary;
import com.yitong.nfc.pboc.tech.cmd.ReadRecord;
import com.yitong.nfc.pboc.tech.cmd.SelectById;
import com.yitong.nfc.pboc.tech.rsp.CreditForLoadRsp;
import com.yitong.nfc.pboc.tech.rsp.GetBalanceRsp;
import com.yitong.nfc.pboc.tech.rsp.GetTransactionProveRsp;
import com.yitong.nfc.pboc.tech.rsp.InitializeForLoadRsp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 功能性IC卡，如公交卡、ETC卡
 * Created by 左克飞 on 2016/11/24 13:01.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.bean.card
 */

public class ICCard extends Card {

    private final String TAG = "ICCard";

    private boolean read0016Flag = false;//读0016文件成功标志

    private final String tag_9F4F = "9F36029F03039F02049C019F4E069A049F2103";//日志文件格式

    private CardHolderMsg cardHolderMsg;
    private CardMakerMsg cardMakerMsg;

    private String lastTransNo;//上次交易序号
    private String lastTAC;//上次交易验证码
    private String lastMAC;//上次交易报文鉴别码

    public ICCard(IsoTag tag) {
        super(tag);
        cardHolderMsg = new CardHolderMsg();
        cardMakerMsg = new CardMakerMsg();
    }

    @Override
    public void initCard() throws IOException, IllegalArgumentException, IllegalStateException {

        ReadBinary readBinary = (ReadBinary) CommandFactory.create(ReadBinary.class);
        try {
            readBinary.sfi(22).index(0).execute(isoTag);//0016,读持卡人基本数据文件,其中，部分卡片，0016文件同0015文件一个层级
            parseFile0016(readBinary.getRsp().getBytes());
            read0016Flag = true;
        } catch (IllegalStateException e) {
            // 这里抛异常，可能是持卡人基本数据文件不存在
        }

        //选择AID
        super.initCard();
        if (tag_4F == null) {//表示选择AID失败，需要手动选择密钥文件
            SelectById seltId = (SelectById) CommandFactory.create(SelectById.class, Constants.SECUKEY_FILE);
            seltId.execute(isoTag);
            ArrayList<BerTLV> seltIdTlvs = seltId.parseTLV();
            BerHouse.printfTLV(seltIdTlvs);

            BerTLV tlv_84 = BerHouse.findTlvByTag(seltIdTlvs, new BerT(TagDict.TAG_84));
            if (tlv_84 != null)
                tag_84 = tlv_84.getVal().toString();
            BerTLV tlv_9F08 = BerHouse.findTlvByTag(seltIdTlvs, new BerT(TagDict.TAG_9F08));
            if (tlv_9F08 != null)
                tag_9F08 = tlv_9F08.getVal().toString();
            BerTLV tlv_9F0C = BerHouse.findTlvByTag(seltIdTlvs, new BerT(TagDict.TAG_9F0C));
            if (tlv_9F0C != null)
                tag_9F0C = tlv_9F0C.getVal().toString();
        }

        if (!read0016Flag) {
            try {
                readBinary.sfi(22).index(0).execute(isoTag);
                parseFile0016(readBinary.getRsp().getBytes());
            } catch (IllegalStateException e) {
                //读持卡人进本信息失败，不影响后续操作，继续处理。
            }
        }

        /**
         * 程序执行到这里，选择了MF（或PSE）、选择了AID（或密钥文件）。
         * 其中，0016（持卡人基本信息文件）同密钥文件一个层级。
         */
        if (tag_9F0C == null) {
            readBinary.sfi(21).index(0).execute(isoTag);//0015 读发卡行基本信息文件
            parseFile0015(readBinary.getRsp().getBytes());
        } else {
            parseFile0015(ByteUtil.hexString2Bytes(tag_9F0C));
        }

        readTransLog();
//        readTransLog("");
    }

    /**
     * 圈存初始化
     * <p>
     * 需要的数据（密钥索引号 1byte|交易金额 4byte|终端机编号 6byte）
     *
     * @param secIndex 密钥索引号 通常是01 16进制表示
     * @param money    圈存金额 int型16进制表示，前位补充0。金额精确到分
     * @param termNo   终端机编号 16进制表示
     * @return 返回圈存初始化结果
     * @throws IOException
     */
    public InitializeForLoadRsp initializeForLoad(String secIndex, String money, String termNo) throws IOException, IllegalStateException {

        StringBuffer data = new StringBuffer();
        data.append(secIndex);// 密钥索引号
        StringBuffer moneyBuffer = new StringBuffer("00000000");
        moneyBuffer.append(ByteUtil.toHex(Integer.parseInt(money)));// 不带小数点表示
        data.append(moneyBuffer.substring(moneyBuffer.length() - 8, moneyBuffer.length()));// 添加金额
        data.append(termNo);// 终端机编号，转16进制

        InitializeForLoad initializeForLoad = (InitializeForLoad) CommandFactory.create(InitializeForLoad.class, ByteUtil.hexString2Bytes(data.toString()));
        initializeForLoad.setP2(Constants.EP).execute(isoTag);
        return (InitializeForLoadRsp) initializeForLoad.getRsp();
    }


    /**
     * 圈存完成
     *
     * @param date 交易日期(主机) yyyyMMdd
     * @param time 交易时间(主机) HHmmss
     * @param mac2 初始化圈存时生成的mac1根据算法标识计算出的mac2，通常由服务端返回。4个字节，这里16进制表示，占用8个字节
     * @throws IOException
     * @throws IllegalStateException
     */
    public CreditForLoadRsp creditForLoad(String date, String time, String mac2) throws IOException, IllegalStateException {

        StringBuffer data = new StringBuffer();
        data.append(date);
        data.append(time);
        data.append(mac2);

        CreditForLoad creditForLoad = (CreditForLoad) CommandFactory.create(CreditForLoad.class, ByteUtil.hexString2Bytes(data.toString()));
        creditForLoad.execute(isoTag);
        return (CreditForLoadRsp) creditForLoad.getRsp();

    }


    /**
     * 读交易日志文件，其中部分卡片需要验PIN
     *
     * @throws IOException
     */
    @Override
    public void readTransLog() throws IOException {

        if (transLog == null)
            transLog = new JSONArray();

        ReadRecord readRecord = (ReadRecord) CommandFactory.create(ReadRecord.class);
        readRecord.sfi(24); //0018 钱包交易记录文件

        for (int i = 1; i <= 50; i++) {//ETC卡最多有50条记录，公交卡最多只有10条记录
            readRecord.index(i).execute(isoTag);
            //交易序号 透支限额 交易金额 交易类型标识 终端机编号 交易时间 交易日期
            //02AB 000000 00000046 06 230001003701 20161112 093559
            try {
                transLog.put(parseLog(ByteUtil.hexString2Bytes(tag_9F4F), readRecord.getRsp().getBytes()));
            } catch (IllegalStateException e) {
                Log.d(TAG, "initCard: " + e);
                break;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            if (transLog.length() > 0) {
                lastTransNo = transLog.getJSONObject(0).getString("tag_9F36");//取最近一次交易记录的交易序号
                String transType = transLog.getJSONObject(0).getString("tag_9C");//取最近一次交易的交易类型
                GetTransactionProve getTransactionProve = (GetTransactionProve) CommandFactory.create(GetTransactionProve.class, ByteUtil.hexString2Bytes(lastTransNo));
                getTransactionProve.transType(ByteUtil.toByte(transType)).execute(isoTag);
                GetTransactionProveRsp getTransactionProveRsp = (GetTransactionProveRsp) getTransactionProve.getRsp();
                lastMAC = getTransactionProveRsp.getMac();
                lastTAC = getTransactionProveRsp.getTac();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读交易日志文件，其中部分卡片需要验PIN
     *
     * @param pin PIN码
     * @throws IOException
     */
    public void readTransLog(String pin) throws IOException {

        this.readTransLog();

    }

    /**
     * 读电子钱包余额
     *
     * @param refresh 区分读圈存完成之前还是读圈存完成之后的余额
     * @return
     */
    @Override
    public double getBalance(boolean refresh) {
        if (!refresh)
            return super.getBalance(refresh);
        GetBalance getBalance = (GetBalance) CommandFactory.create(GetBalance.class);
        getBalance.setP2(Constants.EP);//电子钱包余额
        try {
            getBalance.execute(isoTag);
            GetBalanceRsp getBalanceRsp = (GetBalanceRsp) getBalance.getRsp();
            return getBalanceRsp.getBalance();
        } catch (IOException e) {
            return super.getBalance(refresh);
        }
    }

    /**
     * 解析卡片发型基本数据文件
     *
     * @param data
     */
    private void parseFile0015(byte[] data) {
        if (data.length == 43 || data.length == 50) {
            //C9C2CEF7610100011610610115182200002496392016052620260526C9C2414F4B31313200000000000000
            cardMakerMsg.cardMakerTag = ByteUtil.bytes2HexString(Arrays.copyOfRange(data, 0, 8));// 发卡方标识
            byte cardType = data[8];// 卡片类型21–年/月票卡 22–储值卡 23–记账卡
            // 51–测试用年/月票卡 52–测试用储值卡
            // 53–测试用记账卡
            cardMakerMsg.cardType = cardType;
            if (cardMakerMsg.cardType == 21) {
                cardMakerMsg.cardTypeName = "年/月票卡";
            } else if (cardMakerMsg.cardType == 22) {
                cardMakerMsg.cardTypeName = "储值卡";
            } else if (cardMakerMsg.cardType == 23) {
                cardMakerMsg.cardTypeName = "记账卡";
            } else if (cardMakerMsg.cardType == 51) {
                cardMakerMsg.cardTypeName = "测试用年/月票卡";
            } else if (cardMakerMsg.cardType == 52) {
                cardMakerMsg.cardTypeName = "测试用储值卡";
            } else if (cardMakerMsg.cardType == 53) {
                cardMakerMsg.cardTypeName = "测试用记账卡";
            } else {
                cardMakerMsg.cardTypeName = "";
            }
            cardMakerMsg.cardVersion = ByteUtil.toHex(data[9]);// 记录发行版本号，0x10代表支持复合消费，其它保留
            this.cardVersion = cardMakerMsg.cardVersion;
            cardMakerMsg.cardNetCode = ByteUtil.bytes2HexString(Arrays.copyOfRange(data, 10, 12));// 卡片网络编号
            cardMakerMsg.cardAid = ByteUtil.bytes2HexString(Arrays.copyOfRange(data, 12, 20));// 卡片应用ID
            cardMakerMsg.cardNo = ByteUtil.bytes2HexString(Arrays.copyOfRange(data, 10, 20));// 卡号
            this.cardNo = cardMakerMsg.cardNo;

            cardMakerMsg.valiDateStart = ByteUtil.bytes2HexString(Arrays.copyOfRange(data, 20, 24));// 有效时间
            this.startDate = cardMakerMsg.valiDateStart;
            cardMakerMsg.valiDateEnd = ByteUtil.bytes2HexString(Arrays.copyOfRange(data, 24, 28));// 到期时间
            this.endDate = cardMakerMsg.valiDateEnd;
            try {
                byte[] carCardNumber = Arrays.copyOfRange(data, 28, 40);// 车牌号码
                int lastZero = carCardNumber.length;
                for (int i = 0; i < carCardNumber.length; i++) {
                    if (carCardNumber[i] == 0) {
                        lastZero = i;
                        break;
                    }
                }
                // 车牌号码
                cardMakerMsg.carCardNum = new String(carCardNumber, 0, lastZero, "GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            /**
             * 0–普通用户 1–一类用户 2–二类用户 3–5保留 6–公务车用户 8–军警车用户 10–紧急车用户 12–免费用户
             * 14–车队用户 注： 1～5：为长三角公用预留 6～14：为国标定义 15～20：由各省市自行定义
             */
            byte userType = data[40];// 用户类型
            cardMakerMsg.userType = userType;
            if (cardMakerMsg.userType == 0) {
                cardMakerMsg.userTypeName = "普通用户";
            } else if (cardMakerMsg.userType == 6) {
                cardMakerMsg.userTypeName = "公务车用户";
            } else if (cardMakerMsg.userType == 8) {
                cardMakerMsg.userTypeName = "军警车用户";
            } else if (cardMakerMsg.userType == 10) {
                cardMakerMsg.userTypeName = "紧急车用户";
            } else if (cardMakerMsg.userType == 12) {
                cardMakerMsg.userTypeName = "免费用户";
            } else if (cardMakerMsg.userType == 14) {
                cardMakerMsg.userTypeName = "车队用户";
            } else {
                cardMakerMsg.userTypeName = "保留或自定义用户";
            }
            byte[] carCardColor = Arrays.copyOfRange(data, 41, 43);// 车牌颜色
            // 0–蓝色
            // 1–黄色
            // 2–黑色
            // 3–白色
            int color = carCardColor[0] << 8 | carCardColor[1];
            if (color == 0) {
                cardMakerMsg.carCardColor = CarCardColor.BLUE;
            } else if (color == 1) {
                cardMakerMsg.carCardColor = CarCardColor.YELLOW;
            } else if (color == 2) {
                cardMakerMsg.carCardColor = CarCardColor.BLACK;
            } else if (color == 3) {
                cardMakerMsg.carCardColor = CarCardColor.WHITE;
            } else {
                cardMakerMsg.carCardColor = CarCardColor.OTHER;
            }
        } else if (data.length == 30) {
            //869823000000FFFE0100000023000100(16) 03349143(20) 20141224(24) 20311231(28) 0100(30)
            cardMakerMsg.cardNo = ByteUtil.bytes2HexString(Arrays.copyOfRange(data, 16, 20));
            this.cardNo = cardMakerMsg.cardNo;
            cardMakerMsg.valiDateStart = ByteUtil.bytes2HexString(Arrays.copyOfRange(data, 20, 24));
            startDate = cardMakerMsg.valiDateStart;
            cardMakerMsg.valiDateEnd = ByteUtil.bytes2HexString(Arrays.copyOfRange(data, 24, 28));
            endDate = cardMakerMsg.valiDateEnd;
        }

    }

    /**
     * 解析持卡人基本数据文件
     *
     * @param data
     */
    private void parseFile0016(byte[] data) {
        //0000B8DFCFE800000000000000000000000000000000363130323032313938373130303431323538000000000000000000000000000000
        //00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
        if (data.length == 55) {
            cardHolderMsg.cardHolderIDTag = ByteUtil.toHex(data[0]);// 持卡人身份标识

            cardHolderMsg.sysWorkerTag = ByteUtil.toHex(data[1]);// 本系统职工标识
            byte[] cardHolderName = Arrays.copyOfRange(data, 2, 22);// 持卡人姓名
            byte[] cardHolderIdNumber = Arrays.copyOfRange(data, 22, 54);// 持卡人证件号码
            cardHolderMsg.cardHolderIdType = ByteUtil.toHex(data[54]);// 持卡人证件类型
            this.cardHolderIdType = cardHolderMsg.cardHolderIdType;
            try {
                int lastZero = cardHolderName.length;
                for (int i = 0; i < cardHolderName.length; i++) {
                    if (cardHolderName[i] == 0) {
                        lastZero = i;
                        break;
                    }
                }
                // 解析出持卡人姓名
                cardHolderMsg.cardHolderName = new String(cardHolderName, 0, lastZero, "GBK");
                this.cardHolderName = cardHolderMsg.cardHolderName;
                lastZero = cardHolderIdNumber.length;
                for (int i = 0; i < cardHolderIdNumber.length; i++) {
                    if (cardHolderIdNumber[i] == 0) {
                        lastZero = i;
                        break;
                    }
                }
                // 解析出持卡人身份证号码
                cardHolderMsg.cardHolderIdNumber = new String(cardHolderIdNumber, 0, lastZero);
                this.cardHolderIdNum = cardHolderMsg.cardHolderIdNumber;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 持卡人基本信息
     */
    private class CardHolderMsg {

        private String cardHolderIDTag;// 持卡人身份标识
        private String sysWorkerTag;// 本系统职工标识
        private String cardHolderName;// 持卡人姓名
        private String cardHolderIdNumber;// 持卡人证件号码
        private String cardHolderIdType;// 持卡人证件类型
    }

    /**
     * 卡片发行基本数据
     */
    private class CardMakerMsg {
        private String cardMakerTag;// 卡片发行商标示
        private int cardType;// 卡片类型
        private String cardNo;
        private String cardTypeName;// 卡片类型，名称
        private String cardVersion;// 卡片版本号
        private String cardNetCode;// 卡片网络编号/卡表面编号
        private String cardAid;// 卡片应用id/CPU卡内部编号
        private String valiDateStart;// 有效期起止时间
        private String valiDateEnd;// 有效期结束时间
        private String carCardNum;// 车牌号
        private int userType;// 用户类型
        private String userTypeName;// 用户类型名称
        private CarCardColor carCardColor;// 车牌颜色
    }

    /**
     * 车牌颜色
     */
    public enum CarCardColor {
        BLUE, YELLOW, BLACK, WHITE, OTHER;

        public String toString() {
            if (this == BLUE) {
                return "蓝色";
            } else if (this == YELLOW) {
                return "黄色";
            } else if (this == BLACK) {
                return "黑色";
            } else if (this == WHITE) {
                return "白色";
            } else {
                return "未知";
            }
        }
    }

    @Override
    protected JSONObject getJson() {
        JSONObject json = super.getJson();
        try {
            JSONObject cardHolder = new JSONObject();
            cardHolder.put("cardHolderIdNumber", cardHolderMsg.cardHolderIdNumber);
            cardHolder.put("cardHolderIDTag", cardHolderMsg.cardHolderIDTag);
            cardHolder.put("cardHolderIdType", cardHolderMsg.cardHolderIdType);
            cardHolder.put("cardHolderName", cardHolderMsg.cardHolderName);
            cardHolder.put("sysWorkerTag", cardHolderMsg.sysWorkerTag);
            json.put("cardHolder", cardHolder);

            JSONObject cardMaker = new JSONObject();
            cardMaker.put("carCardColor", cardMakerMsg.carCardColor);
            cardMaker.put("carCardNum", cardMakerMsg.carCardNum);
            cardMaker.put("cardAid", cardMakerMsg.cardAid);
            cardMaker.put("cardMakerTag", cardMakerMsg.cardMakerTag);
            cardMaker.put("cardNetCode", cardMakerMsg.cardNetCode);
            cardMaker.put("cardNo", cardMakerMsg.cardNo);
            cardMaker.put("cardTypeName", cardMakerMsg.cardTypeName);
            cardMaker.put("cardVersion", cardMakerMsg.cardVersion);
            cardMaker.put("cardVersion", cardMakerMsg.cardVersion);
            cardMaker.put("valiDateEnd", cardMakerMsg.valiDateEnd);
            cardMaker.put("valiDateStart", cardMakerMsg.valiDateStart);
            json.put("cardMaker", cardMaker);
//            json.put("lastTransNo", lastTransNo);
//            json.put("lastMAC", lastMAC);
//            json.put("lastTAC", lastTAC);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }


    public String getLastTransNo() {
        return lastTransNo;
    }

    public String getLastTAC() {
        return lastTAC;
    }

    public String getLastMAC() {
        return lastMAC;
    }
}
