package com.yitong.nfc.pboc.tech.cmd;

import java.util.Arrays;

/**
 * 修改个人识别码
 * Created by 左克飞 on 2016/11/25 16:07.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.tech.cmd
 */

public final class ChangePin extends Command {


    /**
     * 修改个人识别码构造函数
     *
     * @param data 格式:当前pin||FF||新pin
     */
    public ChangePin(byte[] data) {
        super(data);
    }


    @Override
    protected byte cla() {
        return (byte) 0x80;
    }

    @Override
    protected byte ins() {
        return (byte) 0x5E;
    }

    @Override
    protected byte p1() {
        return 0x01;
    }

    @Override
    protected byte p2() {
        return 0x00;
    }

    /**
     * 重写获取cmd方法,le不使用
     *
     * @return
     */
    @Override
    public byte[] getCmd() {
        byte[] cmd = super.getCmd();
        return Arrays.copyOf(cmd, cmd.length - 1);
    }
}
