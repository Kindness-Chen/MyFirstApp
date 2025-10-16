package com.example.myfirstapp.rxjava.impl;

/**
 * Date：2025/9/14
 * Time：23:02
 * Author：chenshengrui
 * 被观察者create操作符
 */
public class ObservableCreate<T> extends Observable<T> {
    final ObservableOnSubscribe<T> observableOnSubscribe;

    public ObservableCreate(ObservableOnSubscribe<T> observable0nSubscribe) {
        this.observableOnSubscribe = observable0nSubscribe;
    }

    @Override
    protected void subscribeActual(Observer<T> observer) {
        observer.onSubscribe();
        CreateEmitter<T> tCreateEmitter = new CreateEmitter<T>(observer);
        observableOnSubscribe.subscribe(tCreateEmitter);

    }

    static class CreateEmitter<T> implements Emitter<T> {
        Observer<T> observer;
        boolean done;

        public CreateEmitter(Observer<T> observer) {
            this.observer = observer;
        }

        @Override
        public void onNext(T t) {
            if (done) return;
            observer.onNext(t);
        }

        @Override
        public void onError(Throwable throwable) {
            if (done) return;
            observer.onError(throwable);
            done = true;
        }

        @Override
        public void onComplete() {
            if (done) return;
            observer.onComplete();
            done = true;
        }

    }
}
