package com.example.myfirstapp.rxjava.impl;

import com.example.myfirstapp.rxjava.impl.scheduler.Schedulers;

/**
 * Date：2025/9/14
 * Time：23:12
 * Author：chenshengrui
 */
public class ImplRxJavaTest {

    public static void main(String[] args) {
        Observable.create(new ObservableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(Emitter<Object> emitter) {
                        System.out.println("subscribe thread " + Thread.currentThread());
                        emitter.onNext("12121");
//                emitter.onComplete();
//                emitter.onError(new Throwable());
                        emitter.onComplete();
                    }
                })
                .subscribeOn(Schedulers.newThread())
//                .observeOn(Schedulers.mainThread())
                .map(new Function<Object, Object>() {
                    @Override
                    public Object apply(Object o) {
                        System.out.println("apply thread " + Thread.currentThread());
                        return "map处理后的" + o;
                    }
                })
//                .flatMap(new Function<Object, ObservableSource<Object>>() {
//                    @Override
//                    public ObservableSource<Object> apply(Object o) {
//                        return Observable.create(new ObservableOnSubscribe<Object>() {
//                            @Override
//                            public void subscribe(Emitter<Object> emitter) {
//                                emitter.onNext("flatMap处理后的" + o);
//                                emitter.onComplete();
//                            }
//                        });
//                    }
//                })
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe() {
                        System.out.println("onSubscribe thread " + Thread.currentThread());
                        System.out.println("onSubscribe");
                    }

                    @Override
                    public void onNext(Object o) {
                        System.out.println("onNext thread " + Thread.currentThread());
                        System.out.println("onNext ====" + o);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete thread " + Thread.currentThread());
                        System.out.println("onComplete");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("Throwable thread " + Thread.currentThread());
                        System.out.println(throwable);
                    }
                });
    }
}
