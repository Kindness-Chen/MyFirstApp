package com.yitong.nfc.pboc.tech.ber;

import com.yitong.nfc.ByteUtil;

import java.util.Arrays;

/**
 * 基本编码规则,数据类
 * Created by 左克飞 on 2016/11/24 18:23.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech.ber
 */

public final class BerV extends Ber {

    public BerV() {
        data = new byte[0];
    }

    public BerV(byte[] data) {
        super(data);
    }

    public int read(byte[] src, int start, int len) {
        if (len <= 0) {
            data = new byte[0];
            return start;
        } else {
            data = Arrays.copyOfRange(src, start, start + len);
            return start + len;
        }
    }

    public int toInt(){
        return ByteUtil.toInt(data);
    }
}
