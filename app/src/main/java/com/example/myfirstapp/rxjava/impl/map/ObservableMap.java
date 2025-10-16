package com.example.myfirstapp.rxjava.impl.map;

import com.example.myfirstapp.rxjava.impl.Function;
import com.example.myfirstapp.rxjava.impl.ObservableSource;
import com.example.myfirstapp.rxjava.impl.Observer;

/**
 * Date：2025/9/16
 * Time：10:45
 * Author：chenshengrui
 */
public class ObservableMap<T, U> extends AbstractObservableWithUpStream<T, U> {

    //通过构造方法指定Function
    protected Function<T, U> function;

    public ObservableMap(ObservableSource<T> source, Function<T, U> function) {
        super(source);
        this.function = function;
    }

    @Override
    protected void subscribeActual(Observer<U> observer) {
        source.subscribe(new MapObserver<>(observer, function));
    }

    static class MapObserver<T, U> implements Observer<T> {

        //下游的观察者
        final Observer<U> downStream;
        final Function<T, U> mapper;

        public MapObserver(Observer<U> observer, Function<T, U> function) {
            this.downStream = observer;
            this.mapper = function;
        }

        @Override
        public void onSubscribe() {
            //将 upstream 的 onSubscribe 传递给 downstream
            downStream.onSubscribe();
        }

        @Override
        public void onNext(T t) {
            //mapper操作的核心，通过mapper将T转换成U
            U u = mapper.apply(t);
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
    }
}
