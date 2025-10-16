package com.yitong.utils;

import android.util.Log;

import com.yitong.mbank.util.security.HexUtil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5加密解密
 * @author sven
 *
 */
public class Md5Util {
	/***
     * MD5加码 生成32位md5码
     */
    public static String byte2MD5(byte[] source){
        MessageDigest md5 = null;
        try{
            md5 = MessageDigest.getInstance("MD5");
        }catch (Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
            return "";
        }
        byte[] md5Bytes = md5.digest(source);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++){
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();

    }


    /**
     * 字符串md5编码
     * @param stringData
     * @return
     */
    public static String encode(final String stringData) {
        String md5Result = null;
        try {
            md5Result = encode(stringData.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {

        }
        return md5Result;
    }

    /**a
     * 字节流md5编码
     * @param byteData
     * @return
     */
    public static String encode(final byte[] byteData) {
        String md5Result = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteData);
            md5Result = HexUtil.encode(md5.digest());
            md5.reset();

        } catch (NoSuchAlgorithmException e) {
            Log.e("com.diguotech.Util.MD5", "get md5 str error!");
        }
        return md5Result;
    }

}
