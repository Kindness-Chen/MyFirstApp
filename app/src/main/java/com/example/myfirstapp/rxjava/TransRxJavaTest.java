package com.example.myfirstapp.rxjava;

import com.example.myfirstapp.rxjava.impl.scheduler.SchedulerTransformer;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Date：2025/9/14
 * Time：11:17
 * Author：chenshengrui
 * 转换操作符
 */
public class TransRxJavaTest {

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
        TransRxJavaTest transRxJavaTest = new TransRxJavaTest();
//        transRxJavaTest.test1();
        transRxJavaTest.test3();
    }

    private void test1() {
        //map,直接对发射出来的事件进行处理并转换成新的事件，再次发射
        Observable.just(1, 2, 3)
                .map(new Function<Integer, Object>() {
                    @Override
                    public Object apply(Integer integer) throws Exception {
                        return integer * 2;
                    }
                }).subscribe(observer);
    }

    private void test2() {
        //flatMap,对发射出来的事件进行处理并转换成新的事件，但是 flatMap() 方法会返回一个 Observable 对象，
        // flatMap() 方法会返回一个 Observable 对象，这个 Observable 对象会发射处理后的事件。
        //适用于网络嵌套循环的场景
        Observable.just("注册")
                .flatMap(new Function<String, Observable<?>>() {
                    @Override
                    public Observable<?> apply(String s) throws Exception {
                        System.out.println("注册成功");
                        //返回的结果是无序的（单线程下有序，多线程无序）
                        return Observable.just("登录");
                    }
                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
                //上面两个的代码操作可以同时替换成 compose
                .compose(new SchedulerTransformer())
                .subscribe(observer);

        Observable.just("注册")
                .concatMap(new Function<String, Observable<?>>() {
                    @Override
                    public Observable<?> apply(String s) throws Exception {
                        System.out.println("注册成功");
                        //返回的结果是有序的（单线程下有序，多线程有序）
                        return Observable.just("登录");
                    }
                }).subscribe(observer);
    }

    private void test3() {
        //缓冲，3个事件后一起发送
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .buffer(3)
                .subscribe(observer);
    }
}
