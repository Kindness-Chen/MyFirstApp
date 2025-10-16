package com.example.myfirstapp.rxjava.impl.rxsubject;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfirstapp.rxjava.impl.rxlifecycle.RxLifecycle;
import com.example.myfirstapp.rxjava.impl.scheduler.SchedulerTransformer;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;


public class RxBusActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.get().toObservable(String.class)
                .compose(RxLifecycle.bindLifecycle(this))
                .compose(new SchedulerTransformer())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        System.out.println("accept===" + o);
                    }
                });
    }
}
