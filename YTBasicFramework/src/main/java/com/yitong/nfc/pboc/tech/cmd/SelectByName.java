package com.yitong.nfc.pboc.tech.cmd;

import com.yitong.nfc.pboc.tech.IsoTag;

import java.io.IOException;

/**
 * Created by 左克飞 on 2016/11/24 18:18.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.tech.cmd
 */

public class SelectByName extends Command implements SelectImpl {

    private byte p2 = 0x00;//00表示选择第一个或只有一个,02表示选择下一个

    /**
     * 选择文件构造函数
     * @param data 文件名称,转字节数组
     */
    public SelectByName(byte[] data) {
        super(data);
    }


    @Override
    protected byte cla() {
        return 0x00;
    }

    @Override
    protected byte ins() {
        return (byte) 0xA4;
    }

    @Override
    protected byte p1() {
        return 0x04;
    }

    @Override
    protected byte p2() {
        return p2;
    }

    @Override
    public void selectNext(IsoTag tag) throws IOException {
        p2 = 0x02;
        execute(tag);
    }
}
