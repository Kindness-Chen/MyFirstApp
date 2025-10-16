package com.yitong.nfc.pboc.tech.rsp;

import com.yitong.nfc.ByteUtil;

/**
 * 圈存响应
 * <p>
 * Created by 左克飞 on 2016/11/25 16:34.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech.rsp
 */

public class CreditForLoadRsp extends Response {

    private String tac;//交易验证码


    public CreditForLoadRsp(byte[] cmd, byte[] rsp) {
        super(cmd, rsp);
        if (isOkay())
            tac = ByteUtil.bytes2HexString(getBytes());

    }

    /**
     * 获取交易验证码
     *
     * @return
     */
    public String getTac() {
        return tac;
    }
}
