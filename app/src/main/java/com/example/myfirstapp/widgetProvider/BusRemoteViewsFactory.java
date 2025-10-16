package com.example.myfirstapp.widgetProvider;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.myfirstapp.R;

/**
 * Date：2023/1/3
 * Time：11:44
 * Author：chenshengrui
 */
public class BusRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;

    private int mAppWidgetId;

    /**
     * 构造GridRemoteViewsFactory
     */
    public BusRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }
    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.view_app_widget2);
        // 设置 第position位的“视图”对应的响应事件
//        Intent fillInIntent = new Intent();
//        fillInIntent.putExtra("Type", 0);
//        fillInIntent.putExtra(BusWidgetProvider.REFRESH_WIDGET, position);
//        rv.setOnClickFillInIntent(R.id.img_init, fillInIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
