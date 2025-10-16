package com.yitong.nfc.pboc.tech.cmd;

import com.yitong.nfc.pboc.PbocException;
import com.yitong.nfc.pboc.tech.IsoTag;
import com.yitong.nfc.pboc.tech.rsp.DebitForPurchaseRsp;

import java.io.IOException;

/**
 * 消费/取现交易
 * Created by 左克飞 on 2016/11/25 16:46.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech.cmd
 */

public class DebitForPurchase extends Command {

    /**
     * 消费/取现构造函数
     *
     * @param data 格式:终端交易序号(4byte)|交易日期(4byte)|交易时间(3byte)|MAC1(4byte)
     */
    public DebitForPurchase(byte[] data) {
        super(data, (byte) 0x08);
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
        return (byte) 0x01;
    }

    @Override
    protected byte p2() {
        return (byte) 0x00;
    }

    @Override
    public void execute(IsoTag tag) throws IOException, PbocException {
        rsp = new DebitForPurchaseRsp(getCmd(), tag.transceive(getCmd()));
    }
}
