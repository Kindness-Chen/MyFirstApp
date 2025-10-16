package com.example.myfirstapp.rxjava.impl.scheduler;

/**
 * Date：2025/9/16
 * Time：15:16
 * Author：chenshengrui
 */
public abstract class Scheduler {

    public abstract Worker createWork();

    public interface Worker {
        void scheduler(Runnable runnable);
    }
}
