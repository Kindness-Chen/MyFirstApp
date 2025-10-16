package com.example.myfirstapp.rxjava.impl.rxsubject;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Date：2025/9/16
 * Time：23:05
 * Author：chenshengrui
 * 通过Subject实现EventBus的通信
 */
public class RxBus {
    private final Subject<Object> mbus;

    static final class Holder {
        static final RxBus BUS = new RxBus();
    }


    private RxBus() {
        //toSerialized线程安全
        mbus = PublishSubject.create().toSerialized();
    }

    public static RxBus get() {
        return Holder.BUS;
    }

    //发送事件
    public void post(Object o) {
        mbus.onNext(o);
    }

    //设置被观察者
    public <T> Observable<T> toObservable(Class<T> tClass) {
        return mbus.ofType(tClass);
    }
}
