package com.yitong.mbank.util.security;

import org.apache.commons.codec.binary.Base64;

/**
 * Base64操作工具类
 * 基于org.apache.commons.codec封装
 *
 * @author tongxu_li
 * Copyright (c) 2016 Shanghai P&C Information Technology Co., Ltd.
 */
public class Base64Util {

    /**
     * 字节流转base64字符串
     *
     * @param byteData
     * @return
     */
    public static String encode(final byte[] byteData) {
        return new String(new Base64().encode(byteData));
    }

    /**
     * base64字符串转字节流
     *
     * @param base64StringData
     * @return
     */
    public static byte[] decode(final String base64StringData) {
        return new Base64().decode(base64StringData.getBytes());
    }

    /**
     * base64字节流转原字节流
     *
     * @param base64ByteData
     * @return
     */
    public static byte[] decode(final byte[] base64ByteData) {
        return new Base64().decode(base64ByteData);
    }
}
