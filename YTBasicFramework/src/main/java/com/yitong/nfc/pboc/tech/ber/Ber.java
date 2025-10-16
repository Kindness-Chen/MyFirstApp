package com.yitong.nfc.pboc.tech.ber;

import com.yitong.nfc.ByteUtil;

/**
 * 基本编码规则
 * <p>
 * Created by 左克飞 on 2016/11/24 18:19.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech.ber
 */

public abstract class Ber {

    protected byte[] data;

    public Ber() {
        data = new byte[0];
    }

    public Ber(byte[] data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return ByteUtil.bytes2HexString(data);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Ber)) {
            return false;
        }
        Ber ber = (Ber) o;
        if (this.data.length != ber.data.length)
            return false;
        for (int i = 0; i < this.data.length; i++) {
            if (this.data[i] != ber.data[i])
                return false;
        }
        return true;
    }

    public byte[] getBytes() {
        return data;
    }

    public int size() {
        return data.length;
    }
}
