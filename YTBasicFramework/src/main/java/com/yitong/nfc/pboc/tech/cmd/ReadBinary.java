package com.yitong.nfc.pboc.tech.cmd;

/**
 * 读二进制数据
 * Created by 左克飞 on 2016/11/30 01:58.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech.cmd
 */

public class ReadBinary extends Command {

    private int index = 0;//从文件中读取第一个字节的便宜地址
    private int sfi = 0;//sfi(短文件标示符) 21~30
    private byte cla = 0x00;//命令类别,00 表示普通读取,04 表示需要验证mac

    public ReadBinary() {
        cla = 0x00;
    }

    public ReadBinary(byte[] data) {
        super(data);
        cla = 0x04;
    }

    @Override
    protected byte cla() {
        return cla;
    }

    @Override
    protected byte ins() {
        return (byte)(0xB0);
    }

    @Override
    protected byte p1() {
        return (byte) (0x80 | (sfi & 0x1F));
    }

    @Override
    protected byte p2() {
        return (byte) index;
    }

    /**
     * 从文件中读取第一个字节的偏移地址
     *
     * @param index
     * @return
     */
    public ReadBinary index(int index) {
        this.index = index;
        return this;
    }

    /**
     * 指定短文件标识符
     *
     * @param sfi
     * @return
     */
    public ReadBinary sfi(int sfi) {
        this.sfi = sfi;
        return this;
    }
}
