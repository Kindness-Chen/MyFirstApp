package com.example.myfirstapp.rxjava.impl.rxlifecycle;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfirstapp.rxjava.impl.rxsubject.RxBus;
import com.example.myfirstapp.rxjava.impl.scheduler.SchedulerTransformer;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;


public class LifecycleActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observable.create(new ObservableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                        emitter.onNext("");
                        emitter.onComplete();
                    }
                })
                .compose(new SchedulerTransformer())
                .compose(RxLifecycle.bindLifecycle(this))
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                });

        RxBus.get().post("RxBus发送事件");
    }
}
