package com.example.myfirstapp.widgetProvider;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.myfirstapp.R;
import com.example.myfirstapp.activity.MainActivity;

/**
 * Date：2023/1/3
 * Time：11:24
 * Author：chenshengrui
 */
public class BusWidgetProvider2 extends AppWidgetProvider {

    //    public static final String REFRESH_WIDGET = "android.appwidget.action.APPWIDGET_UPDATE";
//    public static final String REFRESH = "android.appwidget.action.GOD_DAVID_REFRESH";
    public static final String GOD_GRID = "android.appwidget.action.GOD_DAVID_GTIDVIEW";
    public static final String GOD_REFRESH = "android.appwidget.action.GOD_DAVID_REFRESH";
    public static final String GOD_START_ACTIVITY = "android.appwidget.action.GOD_DAVID_START_ACTIVITY";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if (GOD_GRID.equals(action)) {
            // 接受“bt_refresh”的点击事件的广播
            Toast.makeText(context, "点击2成功", Toast.LENGTH_SHORT).show();
        } else if (GOD_START_ACTIVITY.equals(action)) {
            Toast.makeText(context, "点击1成功", Toast.LENGTH_SHORT).show();
        } else if (GOD_REFRESH.equals(action)) {
            Toast.makeText(context, "点击2成功", Toast.LENGTH_SHORT).show();
            Intent mainIntent = new Intent(context, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(mainIntent);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
//        for (int appWidgetId : appWidgetIds) {
//            // 获取AppWidget对应的视图
//            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.view_app_widget);
//            // 设置响应 “按钮(bt_refresh)” 的intent
//            Intent btIntent = new Intent(REFRESH);
//            PendingIntent btPendingIntent = PendingIntent.getBroadcast(context, 0, btIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            remoteViews.setOnClickPendingIntent(R.id.button1, btPendingIntent);
//            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
//        }
//        Intent intent = new Intent(context, MyService.class);
//        intent.putExtra("appWidgetIds", appWidgetIds);
//        context.startService(intent);
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.view_app_widget3);

            /**
             *  显示Topic及点击事件,通过Intent启动Activity，PendingIntent.getActivity();
             */
            // 获取AppWidget对应的视图
            // 设置响应 “按钮(bt_refresh)” 的intent
//            Intent bt1Intent = new Intent(context, BusWidgetProvider.class);
//            bt1Intent.setAction(BusWidgetProvider.GOD_START_ACTIVITY);
////            bt1Intent.setClass(context, BusWidgetProvider.class);
//            PendingIntent bt1PendingIntent = PendingIntent.getBroadcast(context, 0, bt1Intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            remoteViews.setOnClickPendingIntent(R.id.button1, bt1PendingIntent);
//
//            Intent bt2Intent = new Intent(context, MainActivity.class);
//            bt2Intent.setAction(BusWidgetProvider.GOD_REFRESH);
//            bt2Intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////            bt2Intent.setClass(context, BusWidgetProvider.class);
//            PendingIntent bt2PendingIntent = PendingIntent.getActivity(context, 0, bt2Intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            remoteViews.setOnClickPendingIntent(R.id.button2, bt2PendingIntent);
            /**
             *  最后，刷新remoteViews
             */
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }

    }
}
