package com.example.myfirstapp.rxjava;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Date：2025/9/13
 * Time：20:20
 * Author：chenshengrui
 * 创建操作符
 */
public class CreateRxJavaTest {

    private final static CompositeDisposable mCompositeDisposable = new CompositeDisposable();


    //成员观察者
    private final Observer<Object> observable = new Observer<Object>() {
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
        System.out.println("===========");
//        test1();
//        test2();
        CreateRxJavaTest rxJavaTest = new CreateRxJavaTest();
        rxJavaTest.test3();
        System.out.println("===========");
    }


    public static void test1() {
        // 创建一个Observable(被观察者)
        Observable.create(new ObservableOnSubscribe<Object>() {
            //ObservableOnSubscribe 被观察者被订阅时调用
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                //事件发送
                emitter.onNext("Hello World");
                emitter.onNext("Hello RXJava");

                //事件发送错误（与onComplete是互斥关系，不能同时触发）
                emitter.onError(new Exception("发送错误"));

                //时间完成
                emitter.onComplete();
            }
        }).subscribe(new Observer<Object>() {
            //订阅观察者
            @Override
            public void onSubscribe(Disposable d) {
                //被观察者被订阅时调用
                System.out.println("onSubscribe");
            }

            @Override
            public void onNext(Object o) {
                //事件发送时回调
                System.out.println("onNext" + o);
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
        });
    }

    //订阅消费者
    public static void test2() {
        // 创建一个Observable(被观察者)
        Disposable subscribe = Observable.create(new ObservableOnSubscribe<Object>() {
            //ObservableOnSubscribe 被观察者被订阅时调用
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                //事件发送
                emitter.onNext("Hello World");
                emitter.onNext("Hello RXJava");

                //事件发送错误（与onComplete是互斥关系，不能同时触发）
                emitter.onError(new Exception("发送错误"));

                //时间完成
                emitter.onComplete();
            }
        }).subscribe(new Consumer<Object>() {
            //消费者
            @Override
            public void accept(Object o) throws Exception {
                System.out.println("accept" + o);
            }
        }, new Consumer<Throwable>() {
            //消费者异常处理
            @Override
            public void accept(Throwable throwable) throws Exception {
                System.out.println("accept" + throwable);
            }
        });
        mCompositeDisposable.add(subscribe);
        mCompositeDisposable.dispose();
    }

    public void test3() {
        //设置被观察者创建
//        RxJavaPlugins.setOnObservableAssembly(new Function<Observable, Observable>() {
//            @Override
//            public Observable apply(Observable observable) throws Exception {
////                Object[] objects = {"1", "2", "3"};
////                return new ObservableFromArray<Object>(objects);
//                return new Observable<Object>() {
//                    @Override
//                    protected void subscribeActual(Observer<? super Object> observer) {
//                        //事件发送
//                        observer.onNext("Hello World");
//                        observer.onNext("Hello RXJava");
//
//                    }
//                };
//            }
//        });
        //内部创建被观察者，事件发送，最多发送10个事件，内部调用fromArray
        Observable.just("1", "2").subscribe(observable);

        //内部创建被观察者，事件发送，可以发送无限个事件
        Observable.fromArray("1", "2", "3", "5").subscribe(observable);

        //迭代器
        ArrayList<Object> objects = new ArrayList<>();
        objects.add("1");
        objects.add("2");
        objects.add("3");
        Observable.fromIterable(objects).subscribe(observable);

        //Future，线程安全
        Observable.fromFuture(new Future<Object>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                return false;
            }

            @Override
            public boolean isCancelled() {
                return false;
            }

            @Override
            public boolean isDone() {
                return false;
            }

            @Override
            public Object get() throws ExecutionException, InterruptedException {
                //执行返回值
                return "2222";
            }

            @Override
            public Object get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
                return null;
            }
        }).subscribe(observable);

        //Callable，执行返回
        Observable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                //执行返回值
                return "22222";
            }
        }).subscribe(observable);
    }
}
