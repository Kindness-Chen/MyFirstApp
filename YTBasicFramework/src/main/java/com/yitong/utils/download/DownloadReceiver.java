package com.yitong.utils.download;

import java.io.File;

import com.yitong.logs.Logs;
import com.yitong.utils.ToastTools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 接收下载成功后的广播
 * 
 * @author flueky zkf@yitong.com.cn
 * @date 2015-5-31 下午12:10:55
 */
public class DownloadReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String filePath = intent.getStringExtra("file");
		File file = new File(filePath);
		if (file.exists()) {
			// 文件存在，则删除
			file.delete();
		}
	}
}
