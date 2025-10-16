package com.yitong.nfc.pboc.tech.cmd;

/**
 * 根据ARQC生成应用密文
 * <p>
 * Created by 左克飞 on 16/12/13 下午3:11.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech.cmd
 */

public class GenerateACByARQC extends GenerateAC {

    public GenerateACByARQC(byte[] data) {
        super(data);
    }

    @Override
    protected byte p1() {
        return (byte) 0x80;
    }
}
