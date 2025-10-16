package com.yitong.nfc.pboc;

/**
 * Created by 左克飞 on 2016/11/29 17:17.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc
 */

public final class Constants {

    /**
     * 在IC卡上,支付系统环境起始于一个名为“1PAY.SYS.DDF01”的目录定义文件(DDF)。 该DDF在IC
     * 卡上是否存在是可选的,但如果存在,则应遵守JR/T 0025的相关规定。 如果这个DDF存在,那么这个DDF
     * 被映射到卡中的某个DF,这个DF可以是MF,也可以不是。
     */
    public static final byte[] DDF_PSE = "1PAY.SYS.DDF01".getBytes();// 支付系统环境
    /**
     * 中国金融集成电路(IC)卡规范 第 12 部分:非接触式 IC 卡支付规范
     * <p/>
     * MSD(磁条数据模式-Magnetic Stripe Data)和qPBOC(快速借记/贷记应用(quick PBOC),
     * 以保证通过非接触界面进行快速交易)的要求是与接触式EMV(Europay、MasterCard 和 Visa)不同的。
     * 对于应用选择,前两者使用PPSE而后者使用PSE。 当PPSE 被选择后,非接触应用的列表在Select指令的应答中被返回。
     * 在EMV应用中,PSE被选择后,将使用Read Record指令来获得卡上的接触式应用列表。
     */
    public static final byte[] DDF_PPSE = "2PAY.SYS.DDF01".getBytes();// 支付系统环境
    /**
     * 主文件结构：Main File
     */
    public static final byte[] MAIN_FILE = {(byte) 0x3F, (byte) 0x00};
    /**
     * 密钥文件
     */
    public static final byte[] SECUKEY_FILE = {(byte) 0x10, (byte) 0x01};

    public static final String CREDIT_CARD_AID = "A000000333010102";//贷记卡应用ID
    public static final String DEBIT_CARD_AID  = "A000000333010101";//借记卡应用ID


    public static final byte ED = (byte) 0x01;//电子存折
    public static final byte EP = (byte) 0x02;//电子钱包
}
