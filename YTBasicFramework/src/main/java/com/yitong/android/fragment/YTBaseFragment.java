package com.yitong.android.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yitong.android.activity.YTFragmentActivity;
import com.yitong.logs.Logs;
import com.yitong.utils.BitmapResourceManage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.UUID;

import androidx.fragment.app.Fragment;

/**
 * 基础Fragment
 *
 * @author tongxu_li
 *         Copyright (c) 2014 Shanghai P&C Information Technology Co., Ltd.
 */
public abstract class YTBaseFragment extends Fragment {
    private static final String LOG_TAG = "YTBaseFragment";

    protected BitmapResourceManage bitmapResManage;

    protected String fragmentTag = UUID.randomUUID().toString();
    protected View contentView = null;
    protected YTFragmentActivity activity = null;
    protected boolean isFirstLoad = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (YTFragmentActivity) activity;
        bitmapResManage = new BitmapResourceManage(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(getContentLayout(), container, false);
            isFirstLoad = true;
        } else {
            isFirstLoad = false;
            ViewGroup vp = (ViewGroup) contentView.getParent();
            if (null != vp) {
                vp.removeView(contentView);
            }
        }
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isFirstLoad) {
            initView();
            initAction();
            initData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Logs.d(LOG_TAG, this.toString() + "registerKeyDownNotify");
        activity.setTopFragment(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Logs.d(LOG_TAG, this.toString() + "removeKeyDownNotify");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        bitmapResManage.clean();
        bitmapResManage = null;
    }

    public String getFragmentTag() {
        return fragmentTag;
    }

    /**
     * 设置布局文件
     */
    protected abstract int getContentLayout();

    /**
     * 控件初始化
     */
    protected void initView() {
    }

    ;

    /**
     * 事件监听
     */
    protected void initAction() {
    }

    ;

    /**
     * 数据处理
     */
    protected void initData() {
    }

    ;

    /**
     * 处理硬键点击
     * 返回false，事件继续传递
     * 返回true，事件终止
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    /**
     * 后退
     */
    public void finish() {
        activity.popBack();
    }

    /**
     * 查找控件
     */
    public View findViewById(int id) {
        View v = null;
        if (contentView != null) {
            v = contentView.findViewById(id);
        }
        return v;
    }

    /**
     * 启动Fragment
     *
     * @param cls    需要启动Fragment的Class
     * @param params 需要向启动Fragment传递的参数
     */
    public boolean startFragment(Class<?> cls, Bundle params) {
        boolean isSuccess = false;
        try {
            YTBaseFragment fragment = (YTBaseFragment) cls.newInstance();
            if (params != null) {
                fragment.setArguments(params);
            }
            activity.changeFragment(fragment, true);
            isSuccess = true;
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
            isSuccess = false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            isSuccess = false;
        }
        return isSuccess;
    }

    /**
     * 启动Activity
     */
    public void startActivity(Intent intent) {
        activity.startActivity(intent);
    }

    /**
     * 启动Activity，接收返回结果
     */
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }

    /**
     * EventBus监听事件，子类根据需要进行重写
     *
     * @param object
     */
    @Subscribe
    public void onEvent(Object object) {
    }

    public BitmapDrawable getImage(int resId) {
        return activity.getImage(resId);
    }

    /**
     * 读取SD图片
     *
     * @param pathString 文件地址
     * @return
     */
    public BitmapDrawable getDiskBitmapDrawable(String pathString) {
        BitmapDrawable bitmapDrawable = null;
        if (bitmapResManage != null) {
            bitmapDrawable = bitmapResManage.getDiskBitmapDrawable(pathString);
        }
        return bitmapDrawable;
    }

    /**
     * 判断事件出发时间间隔是否超过预定值
     */
    private long lastClickTime;

    protected boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
