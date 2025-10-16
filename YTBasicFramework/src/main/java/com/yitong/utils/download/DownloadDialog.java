package com.yitong.utils.download;

import java.net.HttpURLConnection;

import com.yitong.basic.R;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 下载对话框
 * 
 * @author flueky zkf@yitong.com.cn
 * @date 2015-4-27 上午11:31:34
 */
public class DownloadDialog extends Dialog {

	private ProgressBar pBar;
	private HttpURLConnection connection;// http连接
	private TextView txtTitle;// 下载标题
	private Activity activity;
	private String tips = "下载中（%d%%）";

	public DownloadDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		activity = (Activity) context;
	}

	public DownloadDialog(Context context, int theme) {
		super(context, theme);
		activity = (Activity) context;
	}

	public DownloadDialog(Context context) {
		// 自定义dialog风格
		this(context, R.style.MyDialog);
		activity = (Activity) context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_dialog);
		pBar = (ProgressBar) findViewById(R.id.download_dialog_pbar);
		txtTitle = (TextView) findViewById(R.id.download_dialog_txt_title);
	}

	/**
	 * 设置进度
	 * 
	 * @author flueky zkf@yitong.com.cn
	 * @date 2015-4-27 上午11:31:56
	 * @param progress
	 */
	public void setProgress(final int progress) {
		new Handler().post((new Runnable() {

			@Override
			public void run() {
				if (progress == 0) {
					show();
					pBar.setProgress(progress);
					txtTitle.setText(String.format(tips, 0));
				} else if (progress >= 100) {
					if (pBar != null) {
						pBar.setProgress(100);
						txtTitle.setText(String.format(tips, 100));
					}
					if (isShowing() && !activity.isFinishing()) {
						dismiss();
					}
				} else {
					if (!isShowing() && !activity.isFinishing()) {
						show();
					}
					pBar.setProgress(progress);
					txtTitle.setText(String.format(tips, progress));
				}
			}
		}));
	}

	@Override
	public void show() {
		if (!isShowing() && !activity.isFinishing()) {
			super.show();
			pBar = (ProgressBar) findViewById(R.id.download_dialog_pbar);
			txtTitle = (TextView) findViewById(R.id.download_dialog_txt_title);
		}
	}

	@Override
	public void cancel() {
//		super.cancel();
//		// 取消下载断开连接
//		new Thread() {
//			public void run() {
//				if (connection != null) {
//					connection.disconnect();
//				}
//			};
//		}.start();
	}

	public void setConnection(HttpURLConnection connection) {
		this.connection = connection;
	}

}
