package com.yitong.nfc.pboc.tech.cmd;

import com.yitong.nfc.pboc.PbocException;
import com.yitong.nfc.pboc.tech.IsoTag;
import com.yitong.nfc.pboc.tech.rsp.CreditForLoadRsp;

import java.io.IOException;

/**
 * 圈存
 * Created by 左克飞 on 2016/11/25 16:15.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.tech.cmd
 */

public class CreditForLoad extends Command {


    /**
     * 圈存构造函数
     *
     * @param data 格式:交易日期(4byte)|交易时间(3byte)|MAC2(4byte)
     */
    public CreditForLoad(byte[] data) {
        super(data, (byte) 0x04);
    }


    @Override
    protected byte cla() {
        return (byte) 0x80;
    }

    @Override
    protected byte ins() {
        return (byte) 0x52;
    }

    @Override
    protected byte p1() {
        return 0x00;
    }

    @Override
    protected byte p2() {
        return 0x00;
    }

    @Override
    public void execute(IsoTag tag) throws IOException, PbocException {
        rsp = new CreditForLoadRsp(getCmd(), tag.transceive(getCmd()));
    }
}
