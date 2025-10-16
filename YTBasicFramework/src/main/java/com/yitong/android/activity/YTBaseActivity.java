package com.yitong.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Window;

import com.yitong.basic.R;
import com.yitong.consts.YTInitConstans;
import com.yitong.logs.Logs;
import com.yitong.utils.AntiHijackingUtil;
import com.yitong.utils.BitmapResourceManage;
import com.yitong.utils.ToastTools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * 基础activity
 * 
 * @Description
 * @Author lewis(lgs@yitong.com.cn) 2014-4-29 上午9:07:51
 * @Class BaseActivity Copyright (c) 2014 Shanghai P&C Information Technology
 *        Co.,Ltd. All rights reserved.
 */
public abstract class YTBaseActivity extends Activity {

	protected Activity activity;
	protected BitmapResourceManage bitmapResManage;

	@Override
	protected void onCreate(Bundle savedactivityState) {
		super.onCreate(savedactivityState);
        
		// 取消标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (getContentLayout() != 0) {
			setContentView(getContentLayout());
		}
		EventBus.getDefault().register(this);
		bitmapResManage = new BitmapResourceManage(this);
		activity = this;

		initGui();
		initAction();
		initData();
		
		if (!YTInitConstans.isAppInit()) {
			finish();
			return;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		boolean safe = AntiHijackingUtil.checkActivity(this);
		if (!safe) {
			String strAppName = getResources().getString(R.string.app_name);
			String strHijackingTip = getResources().getString(R.string.HijackingTip);
			ToastTools.showLong(this, strAppName + " " + strHijackingTip);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
		bitmapResManage.clean();
		bitmapResManage = null;
	}
	/**
	 * 设置布局文件
	 */
	protected abstract int getContentLayout();

	/**
	 * 初始化UI
	 * 
	 */
	protected abstract void initGui();

	/**
	 * 初始化事件
	 */
	protected abstract void initAction();

	/**
	 * 初始化数据
	 */
	protected abstract void initData();

	/**
	 * 根据资源id获取值
	 */
	protected String getResString(int resId) {
		return activity.getResources().getString(resId);
	}

	private long lastClickTime;

	/**
	 * 判断事件出发时间间隔是否超过预定值
	 * 
	 */
	public boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 1000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	@Override
	public void startActivity(Intent intent) {
		// 防止连续点击
		if (isFastDoubleClick()) {
			Logs.d("BaseActivity", "startActivity() 重复调用");
			return;
		}
		super.startActivity(intent);
	}

    @Subscribe
	public void onEvent(Object object){
		
	}
	
	public BitmapDrawable getImage(int resId) {
		BitmapDrawable bitmapDrawable = null;
		if (bitmapResManage != null) {
			bitmapDrawable = bitmapResManage.getImage(resId);
		}
		return bitmapDrawable;
	}
	
	/**
	 * 读取SD图片
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
}
