package com.example.myfirstapp.widgetProvider;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.myfirstapp.R;

/**
 * Date：2023/1/4
 * Time：11:12
 * Author：chenshengrui
 */
public class MyService extends Service {

    private int[] appWidgetIds;
    private AppWidgetManager appWidgetManager;

    private Context context;

    private RemoteViews remoteViews;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //拿到更新所需要的内容：appWidgetIds;appWidgetManager;
        appWidgetIds = intent.getIntArrayExtra("appWidgetIds");
        appWidgetManager = AppWidgetManager.getInstance(MyService.this);
        //利用MVP模式，下载数据。备注：如果对MVP不是很了解的话，也可以使用自己的请求方式请求数据即可。
        //另：如果你想学习MVP模式的话：可前往“https://github.com/GodDavide/MVP”查看MVP模式介绍，感谢您的支持。
        Toast.makeText(MyService.this, "正在加载最新数据，请稍等... ...", Toast.LENGTH_SHORT).show();

        //初始化Widget（此时Gridview还没有最新数据，如果是首次创建的话，数据为空，非首次，显示的是上次请求的数据。数据存储在数据库里）
        //为了方便，将更新方法直接写进构造函数里了
        UpdaeWidget(MyService.this, appWidgetIds, appWidgetManager);
        return super.onStartCommand(intent, flags, startId);
    }

    public void UpdaeWidget(Context context, int[] appWidgetIds, AppWidgetManager appWidgetManager) {
        this.context = context;
        this.appWidgetIds = appWidgetIds;
        this.appWidgetManager = appWidgetManager;
        this.remoteViews = new RemoteViews(context.getPackageName(), R.layout.view_app_widget);
        //为了方便，在创建UpdateWidget实例的时候，直接调用UpdateWidgetView();
        UpdateWidgetView();

    }

    public void UpdateWidgetView() {
        for (int appWidgetId : appWidgetIds) {
            /**
             * @author David  created at 2016/8/11 17:37
             *  显示Topic及点击事件,通过Intent启动Activity，PendingIntent.getActivity();
             */
            // 获取AppWidget对应的视图
            // 设置响应 “按钮(bt_refresh)” 的intent
            Intent bt1Intent = new Intent();
            bt1Intent.setAction(BusWidgetProvider.GOD_START_ACTIVITY);
            bt1Intent.setClass(context, BusWidgetProvider.class);
            PendingIntent btPendingIntent = PendingIntent.getBroadcast(context, 0, bt1Intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.button1, btPendingIntent);

            Intent bt2Intent = new Intent();
            bt2Intent.setAction(BusWidgetProvider.GOD_REFRESH);
            bt2Intent.setClass(context, BusWidgetProvider.class);
            PendingIntent bt2PendingIntent = PendingIntent.getBroadcast(context, 0, bt2Intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.button2, bt2PendingIntent);
            /**
             * @author David  created at 2016/8/11 17:37
             *  最后，刷新remoteViews
             */
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }
}
