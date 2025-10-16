package com.example.myfirstapp.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myfirstapp.BuildConfig;
import com.yitong.android.application.YTBaseApplication;
import com.yitong.logs.Logs;

public class MyApplication extends YTBaseApplication {

    private final String TAG = MyApplication.this.getClass().getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        activityLife();
        if (BuildConfig.DEBUG) {
            Logs.setLogLevel(Logs.VERBOSE);
//            LogsHandler.getInstance().saveLogInfo2File(this, "MyFirstAppLogsHandler");
        } else {
            Logs.closeLogs();
        }
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);  //这行代码一定要加
//        MultiDex.install(this);
    }

    //activity的生命周期
    private void activityLife() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
                Log.e(TAG, "Activity Create: " + activity.getClass().getSimpleName());
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                Log.e(TAG, "Activity Remove: " + activity.getClass().getSimpleName());
            }
        });
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
