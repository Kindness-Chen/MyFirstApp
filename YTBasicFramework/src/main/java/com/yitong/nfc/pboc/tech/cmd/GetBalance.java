package com.yitong.nfc.pboc.tech.cmd;

import com.yitong.nfc.pboc.PbocException;
import com.yitong.nfc.pboc.tech.IsoTag;
import com.yitong.nfc.pboc.tech.rsp.GetBalanceRsp;

import java.io.IOException;

/**
 * 查询余额
 * Created by 左克飞 on 2016/11/25 17:10.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech.cmd
 */

public class GetBalance extends Command {

    private byte p2 = (byte) 0x00;//

    public GetBalance() {
        super((byte) (0x04));
    }

    @Override
    protected byte cla() {
        return (byte) 0x80;
    }

    @Override
    protected byte ins() {
        return (byte) 0x5C;
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
    public void setP2(byte p2) {
        this.p2 = p2;
    }

    @Override
    public void execute(IsoTag tag) throws IOException, PbocException {
        rsp = new GetBalanceRsp(getCmd(), tag.transceive(getCmd()));
    }
}
