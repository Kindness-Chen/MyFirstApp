package com.example.myfirstapp.rxjava.impl;

import com.example.myfirstapp.rxjava.impl.map.ObservableFlatMap;
import com.example.myfirstapp.rxjava.impl.map.ObservableMap;
import com.example.myfirstapp.rxjava.impl.scheduler.Scheduler;

/**
 * Date：2025/9/14
 * Time：22:55
 * Author：chenshengrui
 */
public abstract class Observable<T> implements ObservableSource<T> {
    @Override
    public void subscribe(Observer<T> observer) {
        // 和谁建立订阅?
        // 怎么建立订阅?
        //为了保证拓展性，交给具体的开发人员实现。这里提供一个抽象的方法
        subscribeActual(observer);
    }

    protected abstract void subscribeActual(Observer<T> observer);

    //实现创建Observable的create操作符
    public static <T> Observable<T> create(ObservableOnSubscribe<T> observableOnSubscribe) {
        return new ObservableCreate<>(observableOnSubscribe);
    }

    //实现map操作符
    public <R> ObservableMap<T, R> map(Function<T, R> function) {
        return new ObservableMap<>(this, function);
    }

    //实现flatMap操作符
    public <R> ObservableFlatMap<T, R> flatMap(Function<T, ObservableSource<R>> function) {
        return new ObservableFlatMap<>(this, function);
    }

    //实现subscribeOn操作符
    public ObservableSubscribeOn<T> subscribeOn(Scheduler scheduler) {
        return new ObservableSubscribeOn<>(this, scheduler);
    }

    //实现observeOn操作符
    public ObservableObserveOn<T> observeOn(Scheduler scheduler) {
        return new ObservableObserveOn<>(this, scheduler);
    }
}
