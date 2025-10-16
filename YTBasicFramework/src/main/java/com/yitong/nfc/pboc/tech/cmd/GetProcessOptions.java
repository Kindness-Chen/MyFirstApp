package com.yitong.nfc.pboc.tech.cmd;

/**
 * Created by 左克飞 on 16/12/9 上午11:53.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech.cmd
 */

public class GetProcessOptions extends Command {

    public GetProcessOptions(byte[] data) {
        super(data);
    }

    @Override
    protected byte cla() {
        return (byte)0x80;
    }

    @Override
    protected byte ins() {
        return (byte)0xA8;
    }

    @Override
    protected byte p1() {
        return (byte)0x00;
    }

    @Override
    protected byte p2() {
        return (byte)0x00;
    }
}
