package com.yitong.android.application;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.app.Application;

public class YTBaseApplication extends Application {

	public static final String TAG = "BaseApplication";

	protected static YTBaseApplication mApp;

	public static YTBaseApplication getInstance() {
		return mApp;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mApp = this;
	}

	/**
	 * 读取asset目录下json格式的文件
	 * 
	 * @author flueky zkf@yitong.com.cn
	 * @date 2015-4-9 下午5:18:45
	 * @param fileName
	 * @return
	 */
	public String getFromAssets(String fileName) {
		String line = null;
		try {
			InputStreamReader inputReader = new InputStreamReader(
					getApplicationContext().getResources().getAssets()
							.open(fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			// 只读一行
			line = bufReader.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return line;
	}
}
