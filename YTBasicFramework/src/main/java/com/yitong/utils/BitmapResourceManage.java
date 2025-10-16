package com.yitong.utils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.yitong.logs.Logs;
import com.yitong.mbank.util.security.SafeCloseUtils;

/**
 * 图片资源加载管理
 * @author tongxu_li
 * Copyright (c) 2015 Shanghai P&C Information Technology Co., Ltd.
 */
public class BitmapResourceManage {
	private static final String TAG = "BitmapResourceManage";

	private Context context;
	protected ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();

	public BitmapResourceManage(Context context) {
		this.context = context;
	}
	
	public BitmapDrawable getImage(int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		Bitmap bitmap = null;
		BitmapDrawable bitmapDrawable = null;
		try {
			bitmap = BitmapFactory.decodeStream(is, null, opt);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SafeCloseUtils.close(is);
		}
		if (bitmap != null) {
			bitmaps.add(bitmap);
			bitmapDrawable = new BitmapDrawable(context.getResources(), bitmap);
		}
		
		return bitmapDrawable;
	}
	
	
	/**
	 * 读取SD图片
	 * @param context 上下文
	 * @param pathString 文件地址
	 * @return
	 */
	public BitmapDrawable getDiskBitmapDrawable(String pathString) {
		Bitmap bitmap = null;
		try {
			File file = new File(pathString);
			if (file.exists()) {
				bitmap = BitmapFactory.decodeFile(pathString);
			}
		} catch (Exception e) {
			Logs.e("BitmapResourceManage", "getDiskBitmapDrawable is Error!!!");
			Logs.e("BitmapResourceManage", "pathString:["+pathString+"]");
			Logs.e("BitmapResourceManage", "e.getMessage:["+e.getMessage()+"]");
			e.printStackTrace();
		}
		if (null != bitmap) {
			bitmaps.add(bitmap);
			return new BitmapDrawable(context.getResources(), bitmap);
		} else {
			return null;
		}
	}
	
	public void clean() {
		if (bitmaps.size() == 0) {
			return ;
		}
		
		for (int i = 0; i < bitmaps.size(); i++) {
			if (!bitmaps.get(i).isRecycled()) {
				bitmaps.get(i).recycle();
			}
		}
		
		bitmaps.clear();
		System.gc();		
	}
}
