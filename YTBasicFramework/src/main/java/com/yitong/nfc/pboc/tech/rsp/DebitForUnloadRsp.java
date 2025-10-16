package com.yitong.nfc.pboc.tech.rsp;

import com.yitong.nfc.ByteUtil;

/**
 * 圈提响应
 * Created by 左克飞 on 2016/11/25 17:05.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech.rsp
 */

public class DebitForUnloadRsp extends Response {
    private String mac3;

    public DebitForUnloadRsp(byte[] cmd, byte[] rsp) {
        super(cmd, rsp);
        if (isOkay())
            mac3 = ByteUtil.bytes2HexString(getBytes());
    }

    public String getMac3() {
        return mac3;
    }
}
