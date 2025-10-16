package com.example.myfirstapp.rxjava.impl;

/**
 * Date：2025/9/14
 * Time：22:51
 * Author：chenshengrui
 *
 *  观察者
 */
public interface Observer<T> {

    //被观察者与观察者建立联系
    void onSubscribe();

    void onNext(T t);

    void onComplete();

    void onError(Throwable throwable);
}
