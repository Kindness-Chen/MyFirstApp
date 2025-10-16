package com.yitong.nfc;

import java.nio.ByteBuffer;

/**
 * 字节管理工具类
 * <p>
 * Created by 左克飞 on 2016/11/24 12:43.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0
 */

public final class ByteUtil {

    /**
     * 16进制编码字符集
     */
    private static char[] hexSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 16进制字符串转字节
     *
     * @param hex
     * @return
     */
    public static byte toByte(String hex) {
        return (byte) toInt(hex);
    }

    /**
     * 16进制字符串转短整型
     *
     * @param hex
     * @return
     */
    public static short toShort(String hex) {
        return (short) toInt(hex);
    }

    /**
     * 16进制字符串转整型
     *
     * @param hex
     * @return
     */
    public static int toInt(String hex) {
        int result = 0;
        for (int i = 0; i < hex.length(); i++) {
            if (hex.charAt(i) >= '0' && hex.charAt(i) <= '9') {
                result = (result << 4) | (byte) (hex.charAt(i) - '0');
            } else if (hex.charAt(i) >= 'A' && hex.charAt(i) <= 'F') {
                result = (result << 4) | (byte) (hex.charAt(i) - 'A' + 10);
            } else if (hex.charAt(i) >= 'a' && hex.charAt(i) <= 'f') {
                result = (result << 4) | (byte) (hex.charAt(i) - 'a' + 10);
            }
        }
        return result;
    }

    /**
     * 字节数组转端整型
     *
     * @param data
     * @return
     */
    public static short toShort(byte[] data) {
        if(data.length==1)
            return data[0];
        int result = 0;
        for (int i = 0; i < data.length; i++) {
            result = (result << 8) | (data[i] & 0xFF);
        }
        return (short) result;
    }

    /**
     * 字节数组转整型
     *
     * @param data
     * @return
     */
    public static int toInt(byte[] data) {
        if(data.length==1)
            return data[0];
        if(data.length==2)
            return toShort(data);
        int result = 0;
        for (int i = 0; i < data.length; i++) {
            result = (result << 8) | (data[i] & 0xFF);
        }
        return result;
    }

    /**
     * 字节转16进制
     *
     * @param data
     * @return
     */
    public static String toHex(byte data) {
        char h = hexSet[data >> 4 & 0x0F];
        char l = hexSet[data & 0x0F];
        StringBuffer buffer = new StringBuffer();
        buffer.append(h);
        buffer.append(l);
        return buffer.toString();
    }

    /**
     * 短整型转16进制
     *
     * @param data
     * @return
     */
    public static String toHex(short data) {
        return bytes2HexString(toBytes(data));
    }

    /**
     * 整型转16进制
     *
     * @param data
     * @return
     */
    public static String toHex(int data) {
        return bytes2HexString(toBytes(data));
    }

    /**
     * short转字节数组
     *
     * @param data
     * @return
     */
    public static byte[] toBytes(short data) {
        if(data<=Byte.MAX_VALUE&&data>=Byte.MIN_VALUE)
            return new byte[]{(byte)data};
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.put((byte) (data >> 8 & 0xFF));
        buffer.put((byte) (data & 0xFF));
        return buffer.array();
    }

    /**
     * int 转字节数组
     * @param data
     * @return
     */
    public static byte[] toBytes(int data){
        if(data<=Byte.MAX_VALUE&&data>=Byte.MIN_VALUE)
            return new byte[]{(byte)data};
        else if(data<=Short.MAX_VALUE&&data>=Short.MIN_VALUE)
            return toBytes((short)data);

        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put((byte) (data >> 24 & 0xFF));
        buffer.put((byte) (data >> 16 & 0xFF));
        buffer.put((byte) (data >> 8 & 0xFF));
        buffer.put((byte) (data & 0xFF));
        return buffer.array();
    }

    /**
     * 字节数组转16进制字符串
     *
     * @param data
     * @return
     */
    public static String bytes2HexString(byte[] data) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < data.length; i++)
            buffer.append(toHex(data[i]));
        return buffer.toString();
    }

    /**
     * 16进制字符串转字节数组
     *
     * @param hex
     * @return
     */
    public static byte[] hexString2Bytes(String hex) {
        int len = 0;
        byte[] result = null;
        int start = 0;
        if (hex.length() % 2 == 0) {
            result = new byte[hex.length() / 2];
        } else {
            result = new byte[hex.length() / 2 + 1];
            start = 1;
            result[0] = toByte(hex.substring(0, 1));
        }
        for (int i = start; i < hex.length() - 1; i += 2) {
            result[i / 2 + start] = toByte(hex.substring(i, i + 2));
        }
        return result;
    }
}
