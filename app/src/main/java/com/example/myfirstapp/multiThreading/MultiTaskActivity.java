package com.example.myfirstapp.multiThreading;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myfirstapp.R;
import com.example.myfirstapp.activity.MainActivity;
import com.yitong.logs.Logs;

import java.lang.ref.WeakReference;

public class MultiTaskActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    private Button btnWinOne;

    private Button btnWinTwo;

    private int count = 100;

    private Handler handler;

    private Runnable runnable;

    private mHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_task);
        btnWinOne = findViewById(R.id.btn_win_one);
        btnWinTwo = findViewById(R.id.btn_win_two);
        initHandlerTask();
        mHandler = new mHandler(MultiTaskActivity.this);
        btnWinOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                threadBugTicket("窗口1");
//                runnableBugTicket("窗口1");
//                handler.post(runnable);
                mHandler.sendEmptyMessage(1);
            }
        });
        btnWinTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.sendEmptyMessage(2);
            }
        });
        TestCallBackEvent.doSomething(new TestCallBack() {
            @Override
            public void callBack(String message) {
                Logs.e(TAG, message);
            }
        });
    }

    //多线程进行购票
    private void threadBugTicket(String windows) {
        MultiThread multiThread = new MultiThread(windows);
        multiThread.start();
    }

    //多线程进行购票
    private void runnableBugTicket(String windows) {
        MultiRunnable multiRunnable = new MultiRunnable(windows);
        Thread threadOne = new Thread(multiRunnable, "窗口1");
        Thread threadTwo = new Thread(multiRunnable, "窗口2");
        threadOne.start();
        threadTwo.start();
    }

    //handler定时任务
    private void initHandlerTask() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                Logs.i(TAG, "handler start!");
                handler.postDelayed(this, 1000);
            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        mHandler.removeCallbacksAndMessages(null);
    }

    //静态内部类弱应用避免内存泄露
    private static class mHandler extends Handler {
        private final String TAG = getClass().getSimpleName();
        //定义弱引用
        private WeakReference<MultiTaskActivity> mActivity;

        private mHandler(Activity activity) {
            mActivity = new WeakReference<>((MultiTaskActivity) activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    Logs.i(TAG, "Handler 1");
                    Toast.makeText(mActivity.get(), "Handler 1", Toast.LENGTH_SHORT).show();
//                    mActivity.get().startActivity(new Intent(mActivity.get(), MainActivity.class));
//                    mActivity.get().finish();
                    mActivity.get().btnWinOne.setText("smile");
                    break;
                case 2:
                    Logs.i(TAG, "Handler 2");
                    Toast.makeText(mActivity.get(), "Handler 2", Toast.LENGTH_SHORT).show();
                    mActivity.get().mHandler.sendEmptyMessageDelayed(1, 5000);
                    break;
            }
        }
    }
}