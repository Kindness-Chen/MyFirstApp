package com.yitong.nfc.pboc.tech.rsp;

import com.yitong.nfc.pboc.tech.ber.BerL;
import com.yitong.nfc.pboc.tech.ber.BerT;
import com.yitong.nfc.pboc.tech.ber.BerV;

/**
 * 获取数据返回
 * Created by 左克飞 on 16/12/9 下午5:52.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech.rsp
 */

public class GetDataRsp extends Response {

    private String value;

    public GetDataRsp(byte[] cmd, byte[] rsp) {
        super(cmd, rsp);
        BerT berT = new BerT();
        BerL berL = new BerL();
        BerV berV = new BerV();
        byte[] bytes = getBytes();
        berV.read(bytes, berL.read(bytes, berT.read(bytes, 0)), berL.toInt());
        value = berV.toString();
    }

    public String getValue() {
        return value;
    }


}
