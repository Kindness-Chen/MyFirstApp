package com.yitong.nfc.pboc.tech.cmd;

/**
 * 根据sfi(短文件标示符)读记录
 * Created by 左克飞 on 2016/11/25 15:59.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.tech.cmd
 */

public final class ReadRecord extends Command {

    private int index;//记录号
    private int sfi;//短文件标示符

    public ReadRecord() {
    }

    @Override
    protected byte cla() {
        return 0x00;
    }

    @Override
    protected byte ins() {
        return (byte) 0xB2;
    }

    @Override
    protected byte p1() {
        return (byte) index;
    }

    @Override
    protected byte p2() {
        return (byte) (sfi << 3 | 0x04);
    }

    /**
     * 指定记录号
     *
     * @param index
     * @return
     */
    public ReadRecord index(int index) {
        this.index = index;
        return this;
    }

    /**
     * 指定sfi
     *
     * @param sfi
     * @return
     */
    public ReadRecord sfi(int sfi) {
        this.sfi = sfi;
        return this;
    }
}
