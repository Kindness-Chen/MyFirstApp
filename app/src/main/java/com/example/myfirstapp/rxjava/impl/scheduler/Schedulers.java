package com.example.myfirstapp.rxjava.impl.scheduler;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Date：2025/9/16
 * Time：15:44
 * Author：chenshengrui
 */
public class Schedulers {

    private final static Scheduler MAIN_THREAD;

    private final static Scheduler NEW_THREAD;

    static {
        MAIN_THREAD = new HandlerScheduler(new Handler(Looper.getMainLooper()));
        NEW_THREAD = new NewThreadSchedule(Executors.newScheduledThreadPool(2));
    }

    public static Scheduler mainThread() {
        return MAIN_THREAD;
    }

    public static Scheduler newThread() {
        return NEW_THREAD;
    }
}
