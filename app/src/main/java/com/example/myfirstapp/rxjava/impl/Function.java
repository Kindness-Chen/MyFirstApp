package com.example.myfirstapp.rxjava.impl;

/**
 * Date：2025/9/16
 * Time：10:43
 * Author：chenshengrui
 */
public interface Function<T, R> {

    //将T类型转换成R类型
    R apply(T t);
}
