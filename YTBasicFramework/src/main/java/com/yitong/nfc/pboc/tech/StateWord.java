package com.yitong.nfc.pboc.tech;

/**
 * 状态字
 * Created by 左克飞 on 2016/11/25 11:27.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.pboc.tech
 */

public final class StateWord {
    public static final short sw_9000 = (short) 0x9000;// 正确返回，没有错误

    // TODO 后续错误状态，继续添加
    public static final short sw_1000 = (short) 0x1000;// rsp 返回指令异常错误
    public static final short sw_63CX = (short) 0x63C0;// 验证失败,还剩下x次机会
    public static final short sw_6581 = (short) 0x6581;// 内存错误
    public static final short sw_6700 = (short) 0x6700;// 长度错误
    public static final short sw_6882 = (short) 0x6882;// 不支持安全报文
    public static final short sw_6981 = (short) 0x6981;// 命令与文件结构不匹配
    public static final short sw_6982 = (short) 0x6982;// 安全状态不满足
    public static final short sw_6983 = (short) 0x6983;// 验证方法锁定
    public static final short sw_6985 = (short) 0x6985;// 使用条件不满足
    public static final short sw_6986 = (short) 0x6986;// 命令不允许
    public static final short sw_6987 = (short) 0x6987;// 安全报文数据对象丢失
    public static final short sw_6988 = (short) 0x6988;// 安全报文数据对象不正确
    public static final short sw_6A80 = (short) 0x6A80;// 数据域参数不正确
    public static final short sw_6A81 = (short) 0x6A81;// 功能不支持
    public static final short sw_6A82 = (short) 0x6A82;// 文件没找到
    public static final short sw_6A83 = (short) 0x6A83;// 记录没找到
    public static final short sw_6A84 = (short) 0x6A84;// 文件中没有足够空间
    public static final short sw_6A85 = (short) 0x6A85;// Lc和TLV结构不一致
    public static final short sw_6A86 = (short) 0x6A86;// p1和p2参数不正确
    public static final short sw_6D00 = (short) 0x6D00;// INS不支持或错误
    public static final short sw_6E00 = (short) 0x6E00;// CLA不支持或错误
    public static final short sw_6F00 = (short) 0x6F00;// 无准确诊断
    public static final short sw_9302 = (short) 0x9302;// MAC无效

    public static String parseMsg(short sw) {
        String msg;
        switch (sw) {
            case sw_9000:
                msg = "返回指令正常";
                break;
            case sw_1000:
                msg = "返回指令异常错误";
                break;
            case sw_6581:
                msg = "内存错误";
                break;
            case sw_6700:
                msg = "长度错误";
                break;
            case sw_6882:
                msg = "不支持安全报文";
                break;
            case sw_6981:
                msg = "命令与文件结构不匹配";
                break;
            case sw_6982:
                msg = "安全状态不满足";
                break;
            case sw_6983:
                msg = "验证方法锁定";
                break;
            case sw_6985:
                msg = "使用条件不满足";
                break;
            case sw_6986:
                msg = "命令不允许";
                break;
            case sw_6987:
                msg = "安全报文数据对象丢失";
                break;
            case sw_6988:
                msg = "安全报文数据对象不正确";
                break;
            case sw_6A80:
                msg = "数据域参数不正确";
                break;
            case sw_6A81:
                msg = "功能不支持";
                break;
            case sw_6A82:
                msg = "文件没找到";
                break;
            case sw_6A83:
                msg = "记录没找到";
                break;
            case sw_6A84:
                msg = "文件中没有足够空间";
                break;
            case sw_6A85:
                msg = "Lc和TLV结构不一致";
                break;
            case sw_6A86:
                msg = "p1和p2参数不正确";
                break;
            case sw_6D00:
                msg = "INS不支持或错误";
                break;
            case sw_6E00:
                msg = "CLA不支持或错误";
                break;
            case sw_6F00:
                msg = "无准确诊断";
                break;
            case sw_9302:
                msg = "MAC无效";
                break;
            default:
                if ((sw & 0xFFF0) == sw_63CX)
                    msg = "验证失败,还剩下" + (sw & 0x0F) + "次尝试机会";
                 else
                    msg = "未知错误";
                break;
        }
        return msg;
    }
}
