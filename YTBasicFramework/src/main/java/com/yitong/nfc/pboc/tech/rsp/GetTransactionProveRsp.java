package com.yitong.nfc.pboc.tech.rsp;

import com.yitong.nfc.ByteUtil;

import java.util.Arrays;

/**
 * 取交易认证的返回
 * <p>
 * Created by 左克飞 on 16/12/12 下午5:29.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech.rsp
 */

public class GetTransactionProveRsp extends Response {

    private String mac;
    private String tac;

    public GetTransactionProveRsp(byte[] cmd, byte[] rsp) {
        super(cmd, rsp);
        if (getBytes().length == 8) {
            mac = ByteUtil.bytes2HexString(Arrays.copyOfRange(getBytes(), 0, 4));
            tac = ByteUtil.bytes2HexString(Arrays.copyOfRange(getBytes(), 4, 8));
        }
    }

    public String getMac() {
        return mac;
    }

    public String getTac() {
        return tac;
    }
}
