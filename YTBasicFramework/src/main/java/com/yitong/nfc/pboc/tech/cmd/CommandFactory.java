package com.yitong.nfc.pboc.tech.cmd;

import java.lang.reflect.InvocationTargetException;

/**
 * 指令工厂
 * Created by 左克飞 on 2016/11/24 14:32.
 * e-mail:zkf@yitong.com.cn
 * FilePath NFCDemo:com.yitong.nfc2_0.tech.cmd
 */

public class CommandFactory {

    public static <T extends  Command> Command create(Class<T> cls){
        try {
            return  cls.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T extends  Command> Command create(Class<T> cls,byte[] data){
        try {
            return  cls.getConstructor(data.getClass()).newInstance(data);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
