package com.yitong.utils.okhttp3download;

import com.yitong.logs.Logs;
import com.yitong.utils.HttpsUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 带进度监听功能的下载辅助类
 * Created by Jeremy on 2018/1/11.
 */

public class ProgressDownloader {
    public static final String TAG = "ProgressDownloader";

    private ProgressResponseBody.ProgressListener progressListener;

    /**
     * 下载地址
     */
    private String url;
    /**
     * 目标文件
     */
    private File destFile;

    private OkHttpClient client;

    private Call call;

    private boolean isCancel = false;

    public ProgressDownloader(String url, File destFile, ProgressResponseBody.ProgressListener progressListener) {
        this.url = url;
        this.destFile = destFile;
        this.progressListener = progressListener;
        //在下载、暂停后的继续下载中可复用同一个client对象
        client = getProgressClient();
    }

    private OkHttpClient getProgressClient() {
        // 拦截器，用上ProgressResponseBody
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                        .header("RANGE", "bytes=" + 0 + "-")
                        .addHeader("Accept-Encoding", "identity")
                        .build();
            }
        };
        client = new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor)
                .build();
        return client.newBuilder()
                .sslSocketFactory(HttpsUtils.getPassSSLFactory())
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .build();
    }

    public void download() {
        try {
            call = newCall();
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    if (progressListener != null) {
                        progressListener.onError();
                    }
                    Logs.e(TAG, "Callback onFailure: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        save(response);
                    } else {
                        if (progressListener != null) {
                            progressListener.onError();
                        }
                        Logs.e(TAG, "Callback onFailure: " + response.message());
                    }
                }
            });
        } catch (Exception e) {
            if (progressListener != null) {
                progressListener.onError();
            }
            Logs.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * 新建call
     *
     * @return call 对象
     */
    private Call newCall() {
        Request request = new Request.Builder()
                .addHeader("Accept-Encoding", "identity")
                .url(url)
                .build();
        return client.newCall(request);
    }

    /**
     * 停止下载
     */
    public void pause() {
        isCancel = true;
        if (call != null) {
            call.cancel();
        }
    }

    /**
     * 保存文件
     *
     * @param response
     */
    private void save(Response response) {
        ResponseBody body = response.body();
        InputStream in = body.byteStream();
        // 随机访问文件，可以指定断点续传的起始位置
        RandomAccessFile randomAccessFile = null;
        BufferedInputStream bis = null;
        byte[] buff = new byte[2048];
        int len;
        try {
            bis = new BufferedInputStream(in);

            // 随机访问文件，可以指定断点续传的起始位置
            randomAccessFile = new RandomAccessFile(destFile, "rwd");
            randomAccessFile.seek(0);
            while ((len = bis.read(buff)) != -1) {
                randomAccessFile.write(buff, 0, len);
            }
        } catch (IOException e) {
            if (!isCancel && progressListener != null) {
                progressListener.onError();
            }
            Logs.e(TAG, "save IOException 1: " + e.getMessage() + ";isCancel" + isCancel);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                if (!isCancel && progressListener != null) {
                    progressListener.onError();
                }
                Logs.e(TAG, "save IOException 2: " + e.getMessage() + ";isCancel" + isCancel);
            }
        }
    }

}
