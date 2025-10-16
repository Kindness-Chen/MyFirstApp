package com.yitong.nfc.pboc.tech.cmd;

import com.yitong.nfc.pboc.tech.IsoTag;
import com.yitong.nfc.pboc.tech.rsp.InitializeForLoadRsp;

import java.io.IOException;

/**
 * 圈存初始化
 * Created by 左克飞 on 16/12/12 下午6:01.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech.cmd
 */

public class InitializeForLoad extends Command {

    private byte p2 = (byte) 0x00;//

    public InitializeForLoad(byte[] data) {
        super(data, (byte) 0x10);
    }

    @Override
    protected byte cla() {
        return (byte) 0x80;
    }

    @Override
    protected byte ins() {
        return (byte) 0x50;
    }

    @Override
    protected byte p1() {
        return (byte) 0x00;
    }

    @Override
    protected byte p2() {
        return p2;
    }

    /**
     * 设置p2
     *
     * @param p2 取ED 或 EP
     */
    public InitializeForLoad setP2(byte p2) {
        this.p2 = p2;
        return this;
    }

    @Override
    public void execute(IsoTag tag) throws IOException {
        rsp = new InitializeForLoadRsp(getCmd(), tag.transceive(getCmd()));
    }
}
