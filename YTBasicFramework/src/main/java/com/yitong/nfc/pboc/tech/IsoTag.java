package com.yitong.nfc.pboc.tech;

import android.nfc.tech.IsoDep;

import java.io.IOException;

/**
 * nfc 标准通信规范的tag
 * Created by 左克飞 on 2016/11/24 13:14.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech
 */

public class IsoTag {

    private IsoDep isoDep;

    public IsoTag(IsoDep isoDep) {
        this.isoDep = isoDep;
    }

    /**
     * 发送指令
     *
     * @param cmd
     * @return
     * @throws IOException
     */
    public byte[] transceive(byte[] cmd) throws IOException {
        if (!isoDep.isConnected())
            isoDep.connect();
        return isoDep.transceive(cmd);
    }

    /**
     * 关闭tag
     * @throws IOException
     */
    public void close() throws IOException {
        if (isoDep.isConnected())
            isoDep.close();
        return;
    }

}
