package com.example.myfirstapp.rxjava.impl.scheduler;

import java.util.concurrent.ExecutorService;

/**
 * Date：2025/9/16
 * Time：15:48
 * Author：chenshengrui
 */
public class NewThreadSchedule extends Scheduler {

    final ExecutorService mExecutorService;

    public NewThreadSchedule(ExecutorService mExecutorService) {
        this.mExecutorService = mExecutorService;
    }

    @Override
    public Worker createWork() {
        return new NewThreadWorker(mExecutorService);
    }


    static class NewThreadWorker implements Worker {

        final ExecutorService mapper;

        NewThreadWorker(ExecutorService mapper) {
            this.mapper = mapper;
        }


        @Override
        public void scheduler(Runnable runnable) {
            mapper.execute(runnable);
        }
    }
}
