package com.example.myfirstapp.rxjava.impl.scheduler;

import android.os.Handler;

/**
 * Date：2025/9/16
 * Time：15:18
 * Author：chenshengrui
 */
public class HandlerScheduler extends Scheduler {

    final Handler mHandler;

    public HandlerScheduler(Handler mHandler) {
        this.mHandler = mHandler;
    }

    @Override
    public Worker createWork() {
        return new HandlerWorker(mHandler);
    }

    static final class HandlerWorker implements Worker {
        final Handler mapper;

        HandlerWorker(Handler mapper) {
            this.mapper = mapper;
        }

        @Override
        public void scheduler(Runnable runnable) {
            mapper.post(runnable);
        }
    }
}
