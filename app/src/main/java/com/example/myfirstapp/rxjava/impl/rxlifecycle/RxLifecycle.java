package com.example.myfirstapp.rxjava.impl.rxlifecycle;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Date：2025/9/16
 * Time：22:27
 * Author：chenshengrui
 */
public class RxLifecycle<T> implements LifecycleObserver, ObservableTransformer<T, T> {

    final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }

    @Override
    public ObservableSource<T> apply(Observable<T> upstream) {
        return upstream.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                mCompositeDisposable.add(disposable);
            }
        });
    }

    /**
     * 绑定lifecycle
     */
    public static <T> RxLifecycle<T> bindLifecycle(LifecycleOwner lifecycleOwner) {
        RxLifecycle<T> rxLifecycle = new RxLifecycle<>();
        lifecycleOwner.getLifecycle().addObserver(rxLifecycle);
        return rxLifecycle;
    }
}
