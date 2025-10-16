package com.example.myfirstapp.rxjava;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;

/**
 * Date：2025/9/14
 * Time：16:50
 * Author：chenshengrui
 * 过滤操作符
 */
public class FilterRxJavaTest {

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
        FilterRxJavaTest filterRxJavaTest = new FilterRxJavaTest();
        filterRxJavaTest.test1();
    }

    public void test1() {
        Observable.range(1, 10)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer < 5;
                    }
                }).subscribe(observer);
    }
}
