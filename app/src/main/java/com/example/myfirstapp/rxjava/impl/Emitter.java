package com.example.myfirstapp.rxjava.impl;

/**
 * Date：2025/9/14
 * Time：22:58
 * Author：chenshengrui
 * 事件发射器
 */
public interface Emitter<T> {
    void onNext(T t);

    void onComplete();

    void onError(Throwable throwable);
}
