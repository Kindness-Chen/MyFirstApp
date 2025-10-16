package com.yitong.nfc.pboc.tech.cmd;

/**
 * 生成应用密文
 * <p>
 * Created by 左克飞 on 16/12/13 下午3:05.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech.cmd
 */

public abstract class GenerateAC extends Command {

    public GenerateAC(byte[] data) {
        super(data);
    }

    @Override
    protected byte cla() {
        return (byte) 0x80;
    }

    @Override
    protected byte ins() {
        return (byte) 0xAE;
    }


    @Override
    protected byte p2() {
        return (byte) 0x00;
    }
}
