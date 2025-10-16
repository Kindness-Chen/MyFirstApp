package com.yitong.android.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;

/**
 * activity管理
 *
 * @Description
 * @Author lewis(lgs@yitong.com.cn) 2014-4-29 下午2:43:59
 * @Class ActivityTack Copyright (c) 2014 Shanghai P&C Information Technology
 * Co.,Ltd. All rights reserved.
 */
public class YTActivityTack {

    public List<Activity> activityList = new ArrayList<Activity>();

    public static YTActivityTack tack = new YTActivityTack();

    public static YTActivityTack getInstanse() {
        return tack;
    }

    private YTActivityTack() {

    }

    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void removeActivity(Activity activity, boolean finish) {
        if (activity != null) {
            activityList.remove(activity);
            if (finish) {
                activity.finish();
            }
        }
    }

    public Activity getCurrActivity() {
        if (!activityList.isEmpty()) {
            return activityList.get(activityList.size() - 1);
        }
        return null;
    }

    /**
     * 获取界面数量
     *
     * @return activity size
     */
    public int getActivitySize() {
        if (activityList != null) {
            return activityList.size();
        }
        return 0;
    }

    /**
     * 完全退出
     */
    public void exit() {
        while (activityList.size() > 0) {
            popActivity(activityList.get(activityList.size() - 1));
        }
        System.exit(0);
    }

    /**
     * 退出到顶级页面
     *
     * @Description
     * @Author Lewis(lgs@yitong.com.cn) 2014年6月22日 下午10:00:48
     */
    public void exitOnlyOne() {
        while (activityList.size() > 1) {
            popActivity(activityList.get(activityList.size() - 1));
        }
    }

    /**
     * 根据class name获取activity
     *
     * @param name
     * @return
     */
    public Activity getActivityByClassName(String name) {
        for (Activity ac : activityList) {
            if (ac.getClass().getName().contains(name)) {
                return ac;
            }
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    public Activity getActivityByClass(Class cs) {
        for (Activity ac : activityList) {
            if (ac.getClass().equals(cs)) {
                return ac;
            }
        }
        return null;
    }

    /**
     * 弹出activity
     *
     * @param activity
     */
    public void popActivity(Activity activity) {
        removeActivity(activity, true);
    }

    /**
     * 弹出activity到
     *
     * @param cs
     */
    @SuppressWarnings("rawtypes")
    public void popUntilActivity(Class... cs) {
        List<Activity> list = new ArrayList<Activity>();
        for (int i = activityList.size() - 1; i >= 0; i--) {
            Activity ac = activityList.get(i);
            boolean isTop = false;
            for (int j = 0; j < cs.length; j++) {
                if (ac.getClass().equals(cs[j])) {
                    isTop = true;
                    break;
                }
            }
            if (!isTop) {
                list.add(ac);
            } else
                break;
        }
        for (Iterator<Activity> iterator = list.iterator(); iterator.hasNext(); ) {
            Activity activity = iterator.next();
            popActivity(activity);
        }
    }
}
