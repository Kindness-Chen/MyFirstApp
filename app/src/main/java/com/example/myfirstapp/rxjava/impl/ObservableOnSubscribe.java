package com.example.myfirstapp.rxjava.impl;

/**
 * Date：2025/9/14
 * Time：23:00
 * Author：chenshengrui
 */
public interface ObservableOnSubscribe<T> {
    //被观察者与发射器关联
    void subscribe(Emitter<T> emitter);
}
