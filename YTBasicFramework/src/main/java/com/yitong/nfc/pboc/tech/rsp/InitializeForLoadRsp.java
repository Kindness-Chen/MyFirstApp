package com.yitong.nfc.pboc.tech.rsp;

import com.yitong.nfc.ByteUtil;

/**
 * 圈存初始化返回
 * Created by 左克飞 on 16/12/12 下午6:12.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech.rsp
 */

public class InitializeForLoadRsp extends Response {

    private String balance;// ED或EP余额，占用4个字节，这里转成16进制表示占用8个字节 00027C54  0001AD4C
    private String serialNum;// 联机交易序号，占用两个字节，这里转成16进制表示，占用4个字节 0012 0002
    private String keyVer;// 密钥版本号，占用1个字节，这里转成16进制表示，占用2个字节 01 01
    private String algoTag;// 算法标识，占用1个字节，这里转成16进制表示，占用2个字节 00 00
    private String radom;// 伪随机数，占用4个字节，这里转成16进制表示，占用8个字节 E00EB91F 5B9BB719
    private String mac1;// 报文鉴别码1，占用4个字节，转成16进制表示，占用8个字节 DA95FD11  67F07AD5

    public InitializeForLoadRsp(byte[] cmd, byte[] rsp) {
        super(cmd, rsp);
        String rspData = ByteUtil.bytes2HexString(getBytes());
        if (rspData.length() == 32) {
            balance = rspData.substring(0, 8);
            serialNum = rspData.substring(8, 12);
            keyVer = rspData.substring(12, 14);
            algoTag = rspData.substring(14, 16);
            radom = rspData.substring(16, 24);
            mac1 = rspData.substring(24, 32);
        }
    }

    public double getBalance() {
        if (balance == null)
            return 0;
        return ByteUtil.toInt(balance) / 100.0;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public String getKeyVer() {
        return keyVer;
    }

    public String getAlgoTag() {
        return algoTag;
    }

    public String getRadom() {
        return radom;
    }

    public String getMac1() {
        return mac1;
    }
}
