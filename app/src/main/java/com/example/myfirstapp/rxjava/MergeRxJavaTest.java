package com.example.myfirstapp.rxjava;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Date：2025/9/14
 * Time：16:02
 * Author：chenshengrui
 * 组合操作符
 */
public class MergeRxJavaTest {

    private Observer<Object> observer = new Observer<Object>() {
        //订阅观察者
        @Override
        public void onSubscribe(Disposable d) {
            //被观察者被订阅时调用
            System.out.println("onSubscribe");
        }

        @Override
        public void onNext(Object o) {
            //事件发送时回调
            System.out.println("onNext_" + o);
        }

        @Override
        public void onError(Throwable e) {
            System.out.println("onError");
        }

        @Override
        public void onComplete() {
            //时间完成时回调
            System.out.println("onComplete");
        }
    };


    public static void main(String[] args) {
        MergeRxJavaTest mergeRxJavaTest = new MergeRxJavaTest();
        mergeRxJavaTest.test1();

    }

    public void test1() {
        //合并多个Observable
        Observable.concat(Observable.just(1, 2, 3)
                        , Observable.just(4, 5, 6))
                .subscribe(observer);

        Observable.concatArray(Observable.just(1, 2, 3)
                        , Observable.just(4, 5, 6)
                        , Observable.just(4, 5, 6)
                        , Observable.just(4, 5, 6))
                .subscribe(observer);
    }
}
