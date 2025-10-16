package com.yitong.nfc.pboc.tech.cmd;

/**
 * 外部认证
 * <p>
 * Created by 左克飞 on 16/12/13 下午3:18.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech.cmd
 */

public class ExternAuth extends Command {

    public ExternAuth(byte[] data) {
        super(data);
    }

    @Override
    protected byte cla() {
        return (byte) 0x00;
    }

    @Override
    protected byte ins() {
        return (byte) 0x82;
    }

    @Override
    protected byte p1() {
        return (byte) 0x00;
    }

    @Override
    protected byte p2() {
        return (byte) 0x00;
    }
}
