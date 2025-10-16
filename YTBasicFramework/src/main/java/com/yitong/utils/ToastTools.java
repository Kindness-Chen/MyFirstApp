package com.yitong.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * <hr/>
 * 
 * @author 刘国山
 */
public abstract class ToastTools {

	private ToastTools() throws IllegalAccessException {
		throw new IllegalAccessException("工具类无法实例化!");
	}

	public static void showLong(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	public static void showShort(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	public static void showLong(Context context, int resId) {
		Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
	}

	public static void showShort(Context context, int resId) {
		Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Toast显示
	 */
	static Toast toast=null;
	public static void showToast(Context ctx,String msg){
		if(toast==null){
			toast=Toast.makeText(ctx, msg, Toast.LENGTH_LONG);
		}else{
			toast.setText(msg);
		}
		toast.show();
	}
	
	public static void cancel(){
		if(toast != null){
			toast.cancel();
		}
	}
}
