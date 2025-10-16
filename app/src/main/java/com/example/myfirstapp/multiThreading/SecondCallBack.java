package com.example.myfirstapp.multiThreading;

import com.example.myfirstapp.activity.MainActivity;

/**
 * Date：2021/10/26
 * Time：11:41
 * Author：chenshengrui
 */
public class SecondCallBack {

    private static EncryptDelegate encryptDelegate;

    public static void initSecondCallBack(EncryptDelegate encryptDelegate2) {
        encryptDelegate = encryptDelegate2;
    }

    public interface EncryptDelegate {
        String getEncryptString(String decryptString);
    }

    public static String post(String params) {
        return encryptDelegate.getEncryptString(params);
    }
}
