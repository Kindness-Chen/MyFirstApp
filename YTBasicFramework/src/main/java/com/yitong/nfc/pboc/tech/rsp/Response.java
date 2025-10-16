package com.yitong.nfc.pboc.tech.rsp;

import com.yitong.nfc.ByteUtil;
import com.yitong.nfc.pboc.tech.Error;
import com.yitong.nfc.pboc.tech.StateWord;

import java.util.Arrays;

/**
 * 指令执行后返回报文类
 * Created by 左克飞 on 2016/11/24 13:15.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech.rsp
 */

public class Response {

    private byte[] cmd;//指令字节
    private byte[] rsp;//返回数据字节

    public Response(byte[] cmd, byte[] rsp) {
        this.cmd = cmd;
        if (rsp == null || rsp.length < 2)
            this.rsp = ByteUtil.toBytes(StateWord.sw_1000);
        this.rsp = rsp;

    }

    /**
     * 判断响应报文状态
     *
     * @return
     */
    public boolean isOkay() {
        return getSW() == StateWord.sw_9000;

    }

    /**
     * 获取返回报文的错误信息
     * @return
     */
    public Error getError(){
        return new Error(getSW(),StateWord.parseMsg(getSW()));
    }

    /**
     * 获取状态字
     *
     * @return
     */
    public short getSW() {
        short result = 0;
        result = (short) rsp[rsp.length - 2];
        result = (short) (result << 8 | (rsp[rsp.length - 1]&0xFF));
        return result;
    }


    /**
     * 获取响应报文中的有效指令数
     *
     * @return
     */
    public byte[] getBytes() {
        return Arrays.copyOfRange(rsp, 0, rsp.length - 2);
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("cmd:");
        buffer.append(ByteUtil.bytes2HexString(cmd));
        buffer.append("rsp:");
        buffer.append(ByteUtil.bytes2HexString(rsp));
        return buffer.toString();
    }
}
