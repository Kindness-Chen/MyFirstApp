package com.yitong.nfc.pboc.tech.cmd;

import android.util.Log;

import com.yitong.nfc.ByteUtil;
import com.yitong.nfc.pboc.tech.Error;
import com.yitong.nfc.pboc.tech.IsoTag;
import com.yitong.nfc.pboc.tech.ber.BerTLV;
import com.yitong.nfc.pboc.tech.rsp.Response;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * 指令抽象类
 * <p>
 * Created by 左克飞 on 2016/11/24 14:11.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.tech.cmd
 */

public abstract class Command {

    private final String TAG = "Command";

    private byte[] data;//命令数据位串
    private byte lc;//命令数据中存储在的字节数
    private byte le = (byte) 0x00;//响应数据中,期望的最大数据字节数,0表示最长256

    protected Response rsp;

    protected Command() {
        this((byte) 0x00);
    }

    protected Command(byte le) {
        lc = 0;
        this.le = le;
    }

    protected Command(byte[] data) {
        this(data, (byte) 0x00);

    }

    protected Command(byte[] data, byte le) {
        this.le = le;
        this.data = data;
        this.lc = (byte) data.length;
    }

    /**
     * 命令类别
     *
     * @return
     */
    protected abstract byte cla();

    /**
     * 指令代码
     *
     * @return
     */
    protected abstract byte ins();

    /**
     * 指令参数1
     *
     * @return
     */
    protected abstract byte p1();

    /**
     * 指令参数2
     *
     * @return
     */
    protected abstract byte p2();

    /**
     * 生成指令
     *
     * @return
     */
    public byte[] getCmd() {
        ByteBuffer buffer = null;
        if (data == null) {
            buffer = ByteBuffer.allocate(5);
            buffer.put(cla());
            buffer.put(ins());
            buffer.put(p1());
            buffer.put(p2());
            buffer.put(le);
        } else {
            buffer = ByteBuffer.allocate(6 + data.length);
            buffer.put(cla());
            buffer.put(ins());
            buffer.put(p1());
            buffer.put(p2());
            buffer.put(lc);
            buffer.put(data);
            buffer.put(le);
        }
        return buffer.array();
    }

    /**
     * 执行指令
     *
     * @param tag
     * @return
     * @throws IOException
     */
    public void execute(IsoTag tag) throws IOException {
        rsp = new Response(getCmd(), tag.transceive(getCmd()));
        Log.d(TAG, "execute: " + rsp);
    }

    /**
     * 解析响应指令中的tlv值
     *
     * @throws IllegalArgumentException
     */
    public ArrayList<BerTLV> parseTLV() throws IllegalArgumentException, IllegalStateException {
        if (rsp == null)
            throw new IllegalArgumentException("响应指令为空,请先执行execute方法");
        else if (!rsp.isOkay())
            throw new IllegalStateException("响应指令返回状态异常:" + rsp.getError());
        return BerTLV.parse(rsp.getBytes());
    }


    /**
     * 转成字符串显示
     *
     * @return
     */
    @Override
    public String toString() {
        return ByteUtil.bytes2HexString(getCmd());
    }

    /**
     * 获取响应指令
     *
     * @return
     */
    public Response getRsp() throws IllegalArgumentException, IllegalStateException {
        if (rsp == null)
            throw new IllegalArgumentException("响应指令为空,请先执行execute方法");
        else if (!rsp.isOkay())
            throw new IllegalStateException("响应指令返回状态异常:" + rsp.getError());
        return rsp;
    }

    public Error getError() throws IllegalArgumentException {
        if (rsp == null)
            throw new IllegalArgumentException("响应指令为空,请先执行execute方法");
        return rsp.getError();
    }
}
