package com.yitong.nfc.pboc.tech.cmd;

import com.yitong.nfc.pboc.tech.IsoTag;
import com.yitong.nfc.pboc.tech.rsp.GetDataRsp;

import java.io.IOException;

/**
 * 根据标签获取指定的值
 * <p>
 * Created by 左克飞 on 16/12/9 下午5:33.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech.cmd
 */

public class GetData extends Command {

    private byte p1 = 0;
    private byte p2 = 0;

    public GetData() {
    }

    @Override
    protected byte cla() {
        return (byte) 0x80;
    }

    @Override
    protected byte ins() {
        return (byte) 0xCA;
    }

    @Override
    protected byte p1() {
        return p1;
    }

    @Override
    protected byte p2() {
        return p2;
    }

    /**
     * 指定标签
     * @param tag
     * @return
     */
    public GetData setTag(short tag) {
        p1 = (byte) ((tag >> 8) & 0xFF);
        p2 = (byte) (tag & 0xFF);
        return this;
    }

    @Override
    public void execute(IsoTag tag) throws IOException {
        rsp = new GetDataRsp(getCmd(),tag.transceive(getCmd()));
    }
}
