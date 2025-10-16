package com.example.myfirstapp.rxjava.impl;

/**
 * Date：2025/9/14
 * Time：22:48
 * Author：chenshengrui
 * 被观察者顶级接口实现
 */
public interface ObservableSource<T> {

    /**
     * addObserver
     *
     * @param observer
     */
    void subscribe(Observer<T> observer);
}
