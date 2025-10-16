package com.yitong.nfc.pboc.tech.cmd;

import com.yitong.nfc.pboc.tech.IsoTag;

import java.io.IOException;

/**
 * 选择文件接口实现
 * Created by 左克飞 on 2016/11/25 15:25.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.tech.cmd
 */

public interface SelectImpl {
    /**
     * 选择下一个指令
     * @param tag
     * @throws IOException 同nfc 通信异常
     */
    public void selectNext(IsoTag tag) throws IOException;
}
