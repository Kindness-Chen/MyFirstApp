package com.example.myfirstapp.rxjava.impl;

import com.example.myfirstapp.rxjava.impl.map.AbstractObservableWithUpStream;
import com.example.myfirstapp.rxjava.impl.scheduler.Scheduler;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Date：2025/9/16
 * Time：16:22
 * Author：chenshengrui
 */
public class ObservableObserveOn<T> extends AbstractObservableWithUpStream<T, T> {
    final Scheduler scheduler;

    protected ObservableObserveOn(ObservableSource<T> source, Scheduler scheduler) {
        super(source);
        this.scheduler = scheduler;
    }

    @Override
    protected void subscribeActual(Observer<T> observer) {
        Scheduler.Worker worker = scheduler.createWork();
        source.subscribe(new ObserveOnObserve<>(observer, worker));
    }

    static final class ObserveOnObserve<T> implements Observer<T>, Runnable {
        final Observer<T> downStream;
        final Scheduler.Worker worker;
        final Queue<T> queue;

        //处理完成
        volatile boolean done;
        //处理异常
        volatile Throwable error;
        //处理完成
        volatile boolean over;

        ObserveOnObserve(Observer<T> downStream, Scheduler.Worker worker) {
            this.downStream = downStream;
            this.worker = worker;
            this.queue = new ArrayDeque<>();
        }

        @Override
        public void onSubscribe() {

        }

        @Override
        public void onNext(T t) {
            queue.offer(t);//不会抛出异常，只会返回Boolean
            scheduler();

        }

        private void scheduler() {
            worker.scheduler(this);
        }

        @Override
        public void onComplete() {

        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void run() {
            drainNormal();

        }

        /**
         * 从队列中取出数据并下发事件
         */
        private void drainNormal() {
            final Observer<T> downStream = this.downStream;
            final Queue<T> queue = this.queue;

            while (true) {
                boolean d = done;
                T t = queue.poll();//取出数据，不会抛出异常，只会返回对应数据或者 null
                boolean empty = t == null;
                if (checkTerminated(d, empty, downStream)) {
                    return;
                }
                if (empty) {
                    break;
                }
                downStream.onNext(t);
            }
        }

        private boolean checkTerminated(boolean d, boolean empty, Observer<T> downStream) {

            if (over) {
                queue.clear();
                return true;
            }

            if (d) {
                Throwable e = error;
                if (e != null) {
                    over = true;
                    downStream.onError(e);
                    return true;
                } else if (empty) {
                    over = true;
                    downStream.onComplete();
                    return true;
                }
            }
            return false;
        }
    }
}
