package com.example.myfirstapp.synchronizedTest;

import com.yitong.logs.Logs;

public class SynchronizedTest {

    private final String TAG = getClass().getName();

    public synchronized void test() {
        Logs.e(TAG, Thread.currentThread().getName() + "test start...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Logs.e(TAG, Thread.currentThread().getName() + "test end...");
    }

    public void test2() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Logs.e(TAG, Thread.currentThread().getName() + "test2 end...");
    }

    public static void main(String[] args) {
        SynchronizedTest synchronizedTest = new SynchronizedTest();
        new Thread(synchronizedTest::test, "").start();
        new Thread(synchronizedTest::test2, "").start();
    }
}
