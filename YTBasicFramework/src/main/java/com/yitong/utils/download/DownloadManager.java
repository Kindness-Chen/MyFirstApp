package com.yitong.utils.download;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.yitong.service.http.APPRestClient;
import com.yitong.utils.ToastTools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import okhttp3.Cookie;

/**
 * 下载管理。如果需要自动删除下载后的文件，需要注册一个广播test
 * 
 * @author flueky zkf@yitong.com.cn
 * @date 2015-4-27 下午1:34:22
 */
public class DownloadManager {

	private Activity activity;
	private static DownloadManager downloadManager;
	// 下载进度对话框
	private DownloadDialog downloadDialog;
	private String filePath;// 文件路径

	private static final int DOWNLOAD_SUCCEED = 0;// 下载成功，已完成
	private static final int DOWNLOAD_SUCCESS = 1;// 下载成功，正在下载
	private static final int DOWNLOAD_FAILURE = 2;// 下载失败
	private static final int CONNECT_FAILURE = 3;// 下载失败
	private static final int DOWNLOAD_ALREADY = 4;// 下载失败

	public DownloadManager(Activity activity) {
		super();
		this.activity = activity;
	}

	/**
	 * 初始化实例
	 * 
	 * @author flueky zkf@yitong.com.cn
	 * @date 2015-4-27 下午1:13:53
	 * @param activity
	 */
	public static DownloadManager newInstance(Activity activity) {
		if (downloadManager == null) {
			downloadManager = new DownloadManager(activity);
		}
		return downloadManager;
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DOWNLOAD_SUCCEED:
				Toast.makeText(activity, "下载成功", Toast.LENGTH_SHORT).show();
			case DOWNLOAD_ALREADY:
				if (downloadDialog != null && !activity.isFinishing()) {
					downloadDialog.setProgress(100);
					if (activity instanceof DownloadSuccedLinstener) {
						((DownloadSuccedLinstener) activity).downloadSucced();
					}
				}
				// 5分钟后删除文件
				deleteFileAfterTime(5 * 60 * 1000);
				// deleteFileAfterTime(10*1000);
				try {
					Intent openIntent = OpenFileUtil.openFile(filePath);
					activity.startActivity(openIntent);
				} catch (ActivityNotFoundException e) {
					// 未安装应用程序
					ToastTools.showShort(activity, "未安装应用程序");
					e.printStackTrace();
				}
				// openApkFile(filePath);
				break;
			case DOWNLOAD_SUCCESS:
				// 更新进度
				if (downloadDialog != null && !activity.isFinishing()) {
					downloadDialog.setProgress(msg.arg1);
				}
				if (activity instanceof UpdateProcessListener) {
					// 主activity实现了接口，回调进度
					((UpdateProcessListener) activity).updateProcess(msg.arg1);
				}
				break;
			case DOWNLOAD_FAILURE:
				Toast.makeText(activity, "下载失败", Toast.LENGTH_SHORT).show();
				// 下载失败，删除文件
				File file = new File(filePath);
				if (file.exists()) {
					file.delete();
				}
				if (downloadDialog != null && downloadDialog.isShowing()
						&& !activity.isFinishing()) {
					downloadDialog.dismiss();
				}
				break;
			case CONNECT_FAILURE:
				Toast.makeText(activity, "连接失败", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 开始本地下载
	 * 
	 * @author flueky zkf@yitong.com.cn
	 * @date 2015-4-27 下午1:13:15
	 * @param fileUrl
     * @param fileName
	 */
	public void startLocalDownload(final String fileUrl, final String fileName) {

		downloadDialog = new DownloadDialog(activity);
		new Thread() {
			public void run() {
				Looper.prepare();
				try {
					filePath = Environment.getExternalStorageDirectory()
							+ File.separator + fileName;
					File file = new File(filePath);
					if (file.exists()) {
						// 文件已存在，无须下载
						Message msg = new Message();
						msg.what = DOWNLOAD_ALREADY;
						handler.sendMessage(msg);
						return;
					}
					file.createNewFile();

					URL url = new URL(fileUrl);
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					// 同步会话
					List<Cookie> cookies = APPRestClient.getCookies();
					for (int i = 0; i < cookies.size(); i++) {
						connection.setRequestProperty("Cookie", cookies.get(i)
								.name() + "=" + cookies.get(i).value());
					}
					if (connection.getResponseCode() > 400) {
						Message msg = new Message();
						msg.what = CONNECT_FAILURE;
						handler.sendMessage(msg);
						return;
					}
					downloadDialog.setConnection(connection);
					int fileLength = connection.getContentLength();
					InputStream inputStream = connection.getInputStream();
					byte[] buffer = new byte[4096];// 4KB缓存
					// filePath = Environment.getExternalStorageDirectory()
					// + File.separator
					// + fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
					// 缓存在本地

					OutputStream outputStream = new FileOutputStream(file);
					int downLoadedLength = 0;
					while (downLoadedLength < fileLength) {
						int len = inputStream.read(buffer);
						downLoadedLength += len;
						outputStream.write(buffer, 0, len);
						// 更新下载进度
						Message msg = new Message();
						msg.what = DOWNLOAD_SUCCESS;
						msg.arg1 = downLoadedLength * 100 / fileLength;
						handler.sendMessage(msg);
					}
					// 下载成功，发送消息
					Message msg = new Message();
					msg.what = DOWNLOAD_SUCCEED;
					handler.sendMessage(msg);
					inputStream.close();
					outputStream.close();
				} catch (Exception e) {
					// 下载失败
					Log.d("TAG", e.toString());
					Message msg = new Message();
					msg.what = DOWNLOAD_FAILURE;
					handler.sendMessage(msg);
				}
				Looper.loop();
			};

		}.start();

	}

	/**
	 * 下载进度监听器
	 * 
	 * @author flueky zkf@yitong.com.cn
	 * @date 2015-4-27 下午1:46:22
	 */
	public interface UpdateProcessListener {
		/**
		 * 更新下载进度
		 * 
		 * @author flueky zkf@yitong.com.cn
		 * @date 2015-4-27 下午1:46:38
		 * @param process
		 */
		public void updateProcess(int process);
	}

	/**
	 * 打开apk文件进行安装
	 * 
	 * @author flueky zkf@yitong.com.cn
	 * @date 2015-4-27 下午2:54:22
	 * @param filePath
	 */
	private void openApkFile(String filePath) {
		File file = new File(filePath);
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		activity.startActivity(intent);
	}

	/**
	 * 根据指定时间后删除文件
	 * 
	 * @author flueky zkf@yitong.com.cn
	 * @date 2015-5-31 下午1:31:18
	 * @param time
	 */
	private void deleteFileAfterTime(long time) {
		AlarmManager alarmManager = (AlarmManager) activity
				.getSystemService(Activity.ALARM_SERVICE);
		Intent deleteIntent = new Intent(activity, DownloadReceiver.class);
		deleteIntent.putExtra("file", filePath);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 0,
				deleteIntent, 0);
		alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
				SystemClock.elapsedRealtime() + time, pendingIntent);
	}

	/**
	 * 下载成功后的监听
	 * 
	 * @author flueky zkf@yitong.com.cn
	 * @date 2015-5-31 下午1:54:53
	 */
	public interface DownloadSuccedLinstener {
		public void downloadSucced();
	}

	/**
	 * 指定路径下载
	 * @param fileUrl 网络资源url
	 * @param pathStr 路径
	 * @param fileName 文件名
	 * @return 下载是否成功
	 */
	public boolean startLocalToDownload(String fileUrl,
			String pathStr, String fileName, String OriginalSHA1Code) {
		boolean resultBol = false;
		creatSDDir(pathStr);
		File file = null;
		try {
			file = creatSDFile(pathStr, fileName);

			// 构造URL
			URL url = new URL(fileUrl);
			// 打开连接
			URLConnection con = url.openConnection();
			// 获得文件的长度
			int contentLength = con.getContentLength();
			System.out.println("长度 :" + contentLength);
			// 输入流
			InputStream is = con.getInputStream();
			// 1K的数据缓冲
			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len;
			// 输出的文件流
			OutputStream os = new FileOutputStream(file);
			// 开始读取
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			// 完毕，关闭所有链接
			os.close();
			is.close();
			resultBol = true;
		} catch (IOException e) {
			resultBol = false;
			e.printStackTrace();
		}
		return resultBol;

	}

	/**
	 * 在SD卡上创建目录
	 * 
	 * @param dirName
	 */
	public File creatSDDir(String dirName) {
		File dir = new File(dirName);
		if (!dir.exists()) {
			dir.mkdir();
		}
		return dir;
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @throws IOException
	 */
	public File creatSDFile(String pathStr, String fileName) throws IOException {
		File file = new File(pathStr + fileName);
		file.createNewFile();
		return file;
	}
}
