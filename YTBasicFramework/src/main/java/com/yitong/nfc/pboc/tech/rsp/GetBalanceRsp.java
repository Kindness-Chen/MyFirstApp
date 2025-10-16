package com.yitong.nfc.pboc.tech.rsp;

import com.yitong.nfc.ByteUtil;

/**
 * 查询余额指令返回
 * Created by 左克飞 on 2016/11/25 17:16.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech.cmd
 */

public class GetBalanceRsp extends Response {
    double balance;//余额

    public GetBalanceRsp(byte[] cmd, byte[] rsp) {
        super(cmd, rsp);
        if (isOkay())
            balance = ByteUtil.toInt(getBytes())/100.0;
    }

    public double getBalance() {
        return balance;
    }
}
