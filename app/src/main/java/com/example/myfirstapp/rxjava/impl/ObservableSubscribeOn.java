package com.example.myfirstapp.rxjava.impl;

import com.example.myfirstapp.rxjava.impl.map.AbstractObservableWithUpStream;
import com.example.myfirstapp.rxjava.impl.scheduler.Scheduler;

/**
 * Date：2025/9/16
 * Time：15:14
 * Author：chenshengrui
 */
public class ObservableSubscribeOn<T> extends AbstractObservableWithUpStream<T, T> {

    final Scheduler scheduler;

    public ObservableSubscribeOn(ObservableSource<T> source, Scheduler scheduler) {
        super(source);
        this.scheduler = scheduler;
    }

    @Override
    protected void subscribeActual(Observer<T> observer) {
        observer.onSubscribe();
        Scheduler.Worker worker = scheduler.createWork();
        worker.scheduler(new SubscribeTask(new SubscribeOnObserver<>(observer)));
    }

    static class SubscribeOnObserver<T> implements Observer<T> {
        final Observer<T> downStream;

        public SubscribeOnObserver(Observer<T> downStream) {
            this.downStream = downStream;
        }

        @Override
        public void onSubscribe() {
            downStream.onSubscribe();

        }

        @Override
        public void onNext(T t) {
            downStream.onNext(t);
        }

        @Override
        public void onComplete() {
            downStream.onComplete();
        }

        @Override
        public void onError(Throwable throwable) {
            downStream.onError(throwable);
        }
    }

    final class SubscribeTask implements Runnable {
        final SubscribeOnObserver<T> parent;

        public SubscribeTask(SubscribeOnObserver<T> parent) {
            this.parent = parent;
        }

        @Override
        public void run() {
            source.subscribe(parent);
        }
    }

}
