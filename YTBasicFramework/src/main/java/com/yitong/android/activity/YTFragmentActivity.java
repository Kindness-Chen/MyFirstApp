package com.yitong.android.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

import com.yitong.android.fragment.YTBaseFragment;
import com.yitong.basic.R;
import com.yitong.utils.AntiHijackingUtil;
import com.yitong.utils.BitmapResourceManage;
import com.yitong.utils.ToastTools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * 基础FragmentActivity
 * @author tongxu_li
 * Copyright (c) 2015 Shanghai P&C Information Technology Co., Ltd.
 */
public abstract class YTFragmentActivity extends FragmentActivity {
	
	protected BitmapResourceManage bitmapResManage;
	
	protected YTBaseFragment topFragment = null;
	
	private boolean isFragmentBack;
	@Override
	protected void onCreate(Bundle savedactivityState) {
		super.onCreate(savedactivityState);
		// 取消标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		EventBus.getDefault().register(this);
		bitmapResManage = new BitmapResourceManage(this);
		if (getContentLayout() != 0) {
			setContentView(getContentLayout());
		}
		getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
			
			@Override
			public void onBackStackChanged() {
				if(isFragmentBack){
					EventBus.getDefault().post(new BackStackChangeEvent());
				}
				onFragmentChange();
			}
		});
		initView();
		initData();
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
	
	protected abstract int getContentLayout();
	protected abstract void initView();
	protected abstract void initData();
	
	/**
	 * 打开等待层
	 */
	protected void showLoadingView() {};
	/**
	 * 关闭等待层
	 */
	protected void hideLoadingView() {};

	@Override
	public void onBackPressed() {
		if (fragmentCount() <= 0) {
			return;
		}
		isFragmentBack = true;
		super.onBackPressed();
	}
	
	/**
	 * 需要保留，"我的最爱"增加菜单，返回时需要走fragment生命周期，重新加载菜单
	 * @param fragment
	 * @param backStackFlag
	 */
	public void changeFragment(YTBaseFragment fragment, boolean backStackFlag){
		changeFragment(fragment, backStackFlag, true);
	}
	
	public void changeFragment(YTBaseFragment fragment, boolean backStackFlag, boolean isAnimated) {
		changeFragment(R.id.fragments_contain, fragment, backStackFlag, isAnimated);
	}
	
	public void changeFragment(int containId, YTBaseFragment fragment, boolean backStackFlag, boolean isAnimated) {
		isFragmentBack = false;		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		if (isAnimated) {
			ft.setCustomAnimations(
					R.anim.slide_in_right, R.anim.slide_out_left, 
					R.anim.slide_in_left, R.anim.slide_out_right);
		}
		
		ft.replace(containId, fragment, fragment.getFragmentTag());
		if (backStackFlag) {
			ft.addToBackStack(fragment.getFragmentTag());
		} else {
			clearPopBackStack();
		}
		
		ft.commitAllowingStateLoss();
		onFragmentChange();
	}
	
	public void addFragment(YTBaseFragment fragment, boolean backStackFlag) {
		addFragment(fragment, backStackFlag, true);
	}
	
	public void addFragment(YTBaseFragment fragment, boolean backStackFlag, boolean isAnimated) {
		addFragment(R.id.fragments_contain, fragment, backStackFlag, isAnimated);
	}
	
	public void addFragment(int containId, YTBaseFragment fragment, boolean backStackFlag, boolean isAnimated) {
		isFragmentBack = false;		
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		if (isAnimated) {
			ft.setCustomAnimations(
					R.anim.slide_in_right, R.anim.slide_out_left, 
					R.anim.slide_in_left, R.anim.slide_out_right);
		}
		
		ft.add(containId, fragment, fragment.getFragmentTag());
		if (backStackFlag) {
			ft.addToBackStack(fragment.getFragmentTag());
		} else {
			clearPopBackStack();
		}
		
		ft.commitAllowingStateLoss();
		onFragmentChange();
	}

	public int fragmentCount() {
		return getSupportFragmentManager().getBackStackEntryCount();		
	}

	public void popBack() {
		if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
			isFragmentBack = true;
			getSupportFragmentManager().popBackStack();
		}
		onFragmentChange();
	}
	
	public void clearPopBackStack() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.popBackStack(null,
				FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (topFragment != null) {
			boolean isHook = false;
			isHook = topFragment.onKeyDown(keyCode, event);
			if (isHook) {
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void setTopFragment(YTBaseFragment fragment) {
		topFragment = fragment;
	}
	
	protected void onFragmentChange() {
		
	}

    @Subscribe
	public void onEvent(Object object) {
		
	}
	
	public Fragment getCurrentFragment() {
		if (topFragment != null) {
			return topFragment;
		}
		return null;
	}
	
	public class BackStackChangeEvent {
		
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
