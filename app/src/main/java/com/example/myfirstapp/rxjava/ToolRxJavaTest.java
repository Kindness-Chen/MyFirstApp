package com.example.myfirstapp.rxjava;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Date：2025/9/14
 * Time：16:14
 * Author：chenshengrui
 * 工具操作符
 */
public class ToolRxJavaTest {

    public static void main(String[] args) {
        ToolRxJavaTest toolRxJavaTest = new ToolRxJavaTest();
        toolRxJavaTest.test1();
    }

    private void test1() {
        Observable.create(new ObservableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                        System.out.println("subscribe===" + Thread.currentThread());
                        //模式网络请求
                        Thread.sleep(3000);

                        emitter.onNext("1");
                        emitter.onNext("2");
                        emitter.onComplete();

                    }
                }).subscribeOn(Schedulers.newThread())//主要来决定执行subscribe方法所处的线程，也就是产生发生事件的线程
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Object, Object>() {
                    @Override
                    public Object apply(Object o) throws Exception {
                        return "12222";
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println("onSubscribe===" + Thread.currentThread());
                    }

                    @Override
                    public void onNext(Object o) {
                        System.out.println("onNext===" + Thread.currentThread());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete===" + Thread.currentThread());
                    }
                });

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
