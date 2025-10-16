package com.example.myfirstapp.multiThreading;

import com.yitong.logs.Logs;

/**
 * Date：2021/10/18
 * Time：10:33
 * Author：chenshengrui
 * 多线程分别执行不同任务进行抢票
 */
public class MultiThread extends Thread {
    private final String TAG = getClass().getSimpleName();
    private int ticket = 100;
    private String windows;

    public MultiThread(String windows) {
        this.windows = windows;
    }

    @Override
    public void run() {
        while (ticket > 0) {
            ticket--;
            Logs.i(TAG, windows + "购票成功，票数剩余为：" + ticket);
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
