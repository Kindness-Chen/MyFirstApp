package com.yitong.nfc.pboc.tech.ber;

import java.util.Arrays;

/**
 * 基本编码规则,长度类
 * <p>
 * Created by 左克飞 on 2016/11/24 18:22.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech.ber
 */

public final class BerL extends Ber {

    public BerL() {
        data = new byte[0];
    }

    public BerL(byte[] data) {
        super(data);
    }

    /**
     * 读取length标签
     *
     * @param src   源字节数组
     * @param start 起始位置
     * @return 读取的结束位置
     */
    public int read(byte[] src, int start) {
        int len = 0;
        if ((src[len + start] & 0x80) == 0x80) {//第一个字节,最高字节位是1时,后7个字节表示实际长度占用的字节数
            len = (src[len + start] & 0x7F) + 1;
        } else {
            len = 1;
        }
        data = Arrays.copyOfRange(src, start, start + len);
        return start + len;

    }

    /**
     * 获取leng标签实际表示的长度
     *
     * @return
     */
    public int toInt() {
        if (data.length == 1)
            return data[0] & 0x7F;
        int result = 0;
        for (int i = 1; i < data.length; i++) {
            result = (result << 8) | (data[i]&0xFF);
        }
        return result;
    }

    /**
     * 根据实际长度计算出length标签的字节数组
     *
     * @param length
     */
    public void calc(int length) {

    }


}
