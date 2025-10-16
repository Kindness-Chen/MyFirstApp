package com.yitong.nfc.pboc.tech.ber;

import com.yitong.nfc.ByteUtil;

import java.util.Arrays;

/**
 * 基本编码规则,标签类
 * <p>
 * Created by 左克飞 on 2016/11/24 18:22.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech.ber
 */

public final class BerT extends Ber {

    public BerT() {
        data = new byte[0];
    }

    public BerT(byte[] data) {
        super(data);
    }

    public BerT(byte b) {
        super(new byte[]{b});
    }

    public BerT(short s) {
        super(ByteUtil.toBytes(s));
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
        for (len = 0; len < src.length - start; len++) {
            if (len == 0) {
                if ((src[len + start] & 0x1F) < 0x1F)//第一个字节,后五位全部是1 表示还有后续字节
                    break;
            } else {
                if ((src[len + start] & 0x80) != 0x80)//其余字节,高位1 表示还有后续
                    break;
            }
        }
        len++;
        data = Arrays.copyOfRange(src, start, start + len);
        return start + len;

    }

    /**
     * 标签含有子域
     *
     * @return
     */
    protected boolean hasChild() {
        return (data[0] & 0x20) == 0x20;
    }

    public int toInt(){
      return  ByteUtil.toInt(data);
    }
}
