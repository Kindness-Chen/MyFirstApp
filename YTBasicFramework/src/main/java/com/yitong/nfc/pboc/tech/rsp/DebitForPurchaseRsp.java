package com.yitong.nfc.pboc.tech.rsp;

import com.yitong.nfc.ByteUtil;

/**
 * 消费/取现 响应指令
 * Created by 左克飞 on 2016/11/25 16:47.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech.rsp
 */

public class DebitForPurchaseRsp extends Response {

    private  String tac;//交易验证码
    private String mac2;//报文鉴别码

    public DebitForPurchaseRsp(byte[] cmd, byte[] rsp) {
        super(cmd, rsp);
        if(isOkay()){
            String result = ByteUtil.bytes2HexString(getBytes());
            tac = result.substring(0,4);
            mac2 = result.substring(4);
        }
    }

    public String getTac() {
        return tac;
    }

    public String getMac2() {
        return mac2;
    }
}
