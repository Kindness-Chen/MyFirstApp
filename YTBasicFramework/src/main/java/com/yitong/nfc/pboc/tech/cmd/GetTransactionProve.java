package com.yitong.nfc.pboc.tech.cmd;

import com.yitong.nfc.pboc.tech.IsoTag;
import com.yitong.nfc.pboc.tech.rsp.GetTransactionProveRsp;

import java.io.IOException;

/**
 * 取交易认证
 * <p>
 * 根据联机/脱机交易序号取MAC和TAC
 * <p>
 * Created by 左克飞 on 16/12/12 下午5:24.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech.cmd
 */

public class GetTransactionProve extends Command {

    private byte type;

    public GetTransactionProve(byte[] data) {
        super(data, (byte) 0x08);
    }

    @Override
    protected byte cla() {
        return (byte) 0x80;
    }

    @Override
    protected byte ins() {
        return (byte) 0x5A;
    }

    @Override
    protected byte p1() {
        return (byte) 0x00;
    }

    @Override
    protected byte p2() {
        return type;
    }

    /**
     * 指定交易类型
     *
     * @param type
     * @return
     */
    public GetTransactionProve transType(byte type) {
        this.type = type;
        return this;
    }

    @Override
    public void execute(IsoTag tag) throws IOException {
        rsp = new GetTransactionProveRsp(getCmd(), tag.transceive(getCmd()));
    }
}
