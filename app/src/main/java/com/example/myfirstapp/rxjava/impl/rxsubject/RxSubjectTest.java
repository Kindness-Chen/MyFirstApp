package com.example.myfirstapp.rxjava.impl.rxsubject;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.ReplaySubject;

/**
 * Date：2025/9/16
 * Time：22:55
 * Author：chenshengrui
 */
public class RxSubjectTest {

    public static void main(String[] args) {
//       test1();
//        test2();
//        test3();
        test4();
    }

    private static void test3() {
        //接收全部数据
        ReplaySubject<Object> asyncSubject = ReplaySubject.create();
        asyncSubject.onNext("1");
        asyncSubject.onNext("2");
        asyncSubject.subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println("BehaviorSubject==" + o);
            }
        });
        asyncSubject.onNext("3");
        asyncSubject.onNext("4");
        asyncSubject.onComplete();
    }

    private static void test4() {
        //接收订阅后的全部数据
        PublishSubject<Object> asyncSubject = PublishSubject.create();
        asyncSubject.onNext("1");
        asyncSubject.onNext("2");
        asyncSubject.subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println("BehaviorSubject==" + o);
            }
        });
        asyncSubject.onNext("3");
        asyncSubject.onNext("4");
        asyncSubject.onComplete();
    }

    private static void test1() {
        //只会接收到最后一个发送的时间
        AsyncSubject<Object> asyncSubject = AsyncSubject.create();
        asyncSubject.onNext("1");
        asyncSubject.onNext("2");
        asyncSubject.subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println("AsyncSubject==" + o);
            }
        });
        asyncSubject.onNext("3");
        asyncSubject.onComplete();
    }

    private static void test2() {
        //只会接收到订阅之前的最后一条事件和订阅之后的全部事件
        BehaviorSubject<Object> asyncSubject = BehaviorSubject.create();
        asyncSubject.onNext("1");
        asyncSubject.onNext("2");
        asyncSubject.subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                System.out.println("BehaviorSubject==" + o);
            }
        });
        asyncSubject.onNext("3");
        asyncSubject.onNext("4");
        asyncSubject.onComplete();
    }
}
