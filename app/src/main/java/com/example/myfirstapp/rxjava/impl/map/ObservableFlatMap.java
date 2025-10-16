package com.example.myfirstapp.rxjava.impl.map;

import com.example.myfirstapp.rxjava.impl.Function;
import com.example.myfirstapp.rxjava.impl.ObservableSource;
import com.example.myfirstapp.rxjava.impl.Observer;

/**
 * Date：2025/9/16
 * Time：12:01
 * Author：chenshengrui
 */
public class ObservableFlatMap<T, U> extends AbstractObservableWithUpStream<T, U> {

    final ObservableSource<T> source;
    final Function<T, ObservableSource<U>> function;

    public ObservableFlatMap(ObservableSource<T> source, Function<T, ObservableSource<U>> function) {
        super(source);
        this.source = source;
        this.function = function;
    }

    @Override
    protected void subscribeActual(Observer<U> observer) {
        source.subscribe(new FlatMapObserver<>(observer, function));
    }

    static class FlatMapObserver<T, U> implements Observer<T> {

        final Observer<U> downStream;
        final Function<T, ObservableSource<U>> function;

        public FlatMapObserver(Observer<U> observer, Function<T, ObservableSource<U>> function) {
            this.downStream = observer;
            this.function = function;
        }

        @Override
        public void onSubscribe() {
            downStream.onSubscribe();
        }

        @Override
        public void onNext(T t) {
            ObservableSource<U> observable = function.apply(t);
            observable.subscribe(new Observer<U>() {
                @Override
                public void onSubscribe() {
                    downStream.onSubscribe();
                }

                @Override
                public void onNext(U u) {
                    downStream.onNext(u);
                }

                @Override
                public void onComplete() {
                    downStream.onComplete();
                }

                @Override
                public void onError(Throwable throwable) {
                    downStream.onError(throwable);
                }
            });

        }

        @Override
        public void onComplete() {

        }

        @Override
        public void onError(Throwable throwable) {

        }
    }
}
