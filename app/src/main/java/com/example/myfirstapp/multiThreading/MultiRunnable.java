package com.example.myfirstapp.multiThreading;

import com.yitong.logs.Logs;

/**
 * Date：2021/10/18
 * Time：11:09
 * Author：chenshengrui
 */
public class MultiRunnable implements Runnable {
    private final String TAG = getClass().getSimpleName();
    private int ticket = 100;
    private String windows;

    public MultiRunnable(String windows) {
        this.windows = windows;
    }

    @Override
    public void run() {
        while (ticket > 0){
            ticket--;
            Logs.i(TAG, Thread.currentThread().getName() + "购票成功，票数剩余为：" + ticket);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
