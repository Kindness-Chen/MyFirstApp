package com.yitong.nfc.pboc.tech.cmd;

import com.yitong.nfc.pboc.PbocException;
import com.yitong.nfc.pboc.tech.IsoTag;
import com.yitong.nfc.pboc.tech.rsp.DebitForUnloadRsp;

import java.io.IOException;

/**
 * 圈提
 * <p>
 * Created by 左克飞 on 2016/11/25 17:03.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech.cmd
 */

public class DebitForUnload extends Command {

    /**
     * 圈提构造函数
     *
     * @param data 格式:交易日期(4byte)|交易时间(3byte)|MAC2(4byte)
     */
    public DebitForUnload(byte[] data) {
        super(data, (byte) 0x04);
    }

    @Override
    protected byte cla() {
        return (byte) 0x80;
    }

    @Override
    protected byte ins() {
        return (byte) 0x54;
    }

    @Override
    protected byte p1() {
        return (byte) 0x03;
    }

    @Override
    protected byte p2() {
        return (byte) 0x00;
    }

    @Override
    public void execute(IsoTag tag) throws IOException, PbocException {
        rsp = new DebitForUnloadRsp(getCmd(), tag.transceive(getCmd()));
    }
}
