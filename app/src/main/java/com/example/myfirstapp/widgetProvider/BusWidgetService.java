package com.example.myfirstapp.widgetProvider;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Date：2023/1/3
 * Time：11:52
 * Author：chenshengrui
 */
public class BusWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BusRemoteViewsFactory(this, intent);
    }
}
