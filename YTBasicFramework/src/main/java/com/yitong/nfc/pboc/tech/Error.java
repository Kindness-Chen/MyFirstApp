package com.yitong.nfc.pboc.tech;

import com.yitong.nfc.ByteUtil;

/**
 * 错误信息类
 * Created by 左克飞 on 2016/11/25 15:36.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech
 */

public class Error {

    private final String errorFormat  = "{\"errCode\":\"%s\",\"errMsg\":\"%s\"}";

    private short errCode;//错误码

    private String errMsg;//错误信息

    public Error(short errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public String toString() {
        return String.format(errorFormat, ByteUtil.toHex(errCode),errMsg);
    }
}
