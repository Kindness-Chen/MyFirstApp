package com.example.myfirstapp.rxjava.impl.map;

import com.example.myfirstapp.rxjava.impl.Observable;
import com.example.myfirstapp.rxjava.impl.ObservableSource;

/**
 * Date：2025/9/16
 * Time：10:34
 * Author：chenshengrui
 * 扩展类
 */
public abstract class AbstractObservableWithUpStream<T, U> extends Observable<U> {

    protected final ObservableSource<T> source;

    protected AbstractObservableWithUpStream(ObservableSource<T> source) {
        this.source = source;
    }

}
