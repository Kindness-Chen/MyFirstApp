package com.example.networktest.activity;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.networktest.R;
import com.example.networktest.vo.CommonItem;
import com.google.gson.Gson;
import com.yitong.logs.Logs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Date：2025/10/15
 * Time：16:30
 * Author：chenshengrui
 */
public class OkHttpActivity extends AppCompatActivity {
    Button btn;
    Button btn2;
    Button btn3;
    Button btn4;

    private static final String TAG = "OkHttpActivity";
    private static final String BASE_URL = "http://192.168.1.9:9102";


    class MyHandler extends Handler {
        public MyHandler() {
            super();
        }

        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Logs.i(TAG, "handleMessage thread ==>> " + Thread.currentThread().getName());
            Logs.i(TAG, "当前线程ID: " + Thread.currentThread().getId() + ", 主线程ID: " + getMainLooper().getThread().getId());
            btn.setText("okHttp122");
            ViewGroup.LayoutParams layoutParams = btn.getLayoutParams();
            layoutParams.height = 200;
            layoutParams.width = 200;
            Logs.i(TAG, "开始堵塞3" + Thread.currentThread().getName());
            btn.setLayoutParams(layoutParams);
//            Looper.loop();
            Logs.i(TAG, "结束堵塞4" + Thread.currentThread().getName());
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);

        btn = findViewById(R.id.okHttp);
        btn.setOnClickListener(v -> {
            //这个是远顺风顺水的
						复旦复华的返回点击
        });

        btn2 = findViewById(R.id.okHttpPost);
        btn2.setOnClickListener(v -> {
            okHttpRequestPost3333();
        });

        btn3 = findViewById(R.id.uploadFile);
        btn3.setOnClickListener(v -> {
            okHttpUploadFile();
        });

        btn4 = findViewById(R.id.downloadFile);
        btn4.setOnClickListener(v -> {
            okHttpDownloadFile();
        });

//        new Thread(() -> {
//            Looper.prepare();
        Logs.i(TAG, "开始堵塞1" + Thread.currentThread().getName());
        MyHandler myHandler = new MyHandler();
        Message msg = Message.obtain();
        myHandler.sendMessageDelayed(msg, 1000);
        Logs.i(TAG, "结束堵塞2" + Thread.currentThread().getName());
        Logs.i(TAG, "进行循环读取消息，看看是否打印");
//        Looper.loop();
//        }).start();
    }

    private void okHttpDownloadFile() {
        //创建客户端
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .get()
                .url(BASE_URL + "/download/11")
                .build();
        Call call = okHttpClient.newCall(request);
        //异步
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                FileOutputStream fileOutputStream = null;
//                InputStream inputStream = null;
//                try {
//                    Headers headers = response.headers();
//                    for (int i = 0; i < headers.size(); i++) {
//                        String name = headers.name(i);
//                        String value = headers.value(i);
//                        Logs.d(TAG, "Callback onResponse: " + name + ":" + value);
//                    }
//                    String contentType = headers.get("Content-disposition");
//                    String fileName = contentType.replace("attachment; filename=", "");
//                    File file = new File(getBaseContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//                            + File.separator + fileName);
//                    if (!file.getParentFile().exists()) {
//                        file.getParentFile().mkdirs();
//                    }
//                    if (!file.exists()) {
//                        file.createNewFile();
//                    }
//                    inputStream = response.body().byteStream();
//                    byte[] buffer = new byte[1024];
//                    fileOutputStream = new FileOutputStream(file);
//                    int len = 0;
//                    while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
//                        fileOutputStream.write(buffer, 0, len);
//                    }
//                    fileOutputStream.flush();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    if (fileOutputStream != null) {
//                        try {
//                            fileOutputStream.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    if (inputStream != null) {
//                        try {
//                            inputStream.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        });

        new Thread(() -> {
            FileOutputStream fileOutputStream = null;
            InputStream inputStream = null;
            //同步
            try {
                Response response = call.execute();
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    String name = headers.name(i);
                    String value = headers.value(i);
                    Logs.d(TAG, "Callback onResponse: " + name + ":" + value);
                }
                String contentType = headers.get("Content-disposition");
                String fileName = contentType.replace("attachment; filename=", "");
                File file = new File(getBaseContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                        + File.separator + fileName);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                if (!file.exists()) {
                    file.createNewFile();
                }
                inputStream = response.body().byteStream();
                byte[] buffer = new byte[1024];
                fileOutputStream = new FileOutputStream(file);
                int len = 0;
                while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
                    fileOutputStream.write(buffer, 0, len);
                }
                fileOutputStream.flush();

            } catch (
                    Exception e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }).start();
    }

    private void okHttpUploadFile() {
        //创建客户端
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        File file = new File(this.getBaseContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                + File.separator + "10.png");
        MediaType fileType = MediaType.parse("image/png");
        RequestBody fileBody = RequestBody.create(fileType, file);
        RequestBody requestBody = new MultipartBody.Builder()
                .addFormDataPart("file", file.getName(), fileBody)
                .build();
        //创建请求
        Request request = new Request.Builder()
                .url(BASE_URL + "/file/upload")
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Logs.d(TAG, "IOException: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (response.isSuccessful()) {
                        int code = response.code();
                        Logs.d(TAG, "Callback onResponse: " + code);
                        String body = response.body().string();
                        Logs.d(TAG, "Callback onResponse: " + body);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void okHttpRequest() {
        //创建客户端
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        //创建请求
        Request request = new Request.Builder()
                .url(BASE_URL + "/get/text")
                .get()
                .build();
        //创建请求任务
        Call task = okHttpClient.newCall(request);
        //执行请求
        task.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.isSuccessful()) {
                        int code = response.code();
                        Logs.d(TAG, "Callback onResponse: " + code);
                        String body = response.body().string();
                        Logs.d(TAG, "Callback onResponse: " + body);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void okHttpRequestPost() {
        //创建客户端
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        CommonItem hello = new CommonItem("1", "hello");
        Gson gson = new Gson();
        String json = gson.toJson(hello);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        //创建请求
        Request request = new Request.Builder()
                .url(BASE_URL + "/post/comment")
                .post(requestBody)
                .build();
        //创建请求任务
        Call task = okHttpClient.newCall(request);
        //执行请求
        task.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    if (response.isSuccessful()) {
                        int code = response.code();
                        Logs.d(TAG, "Callback onResponse: " + code);
                        String body = response.body().string();
                        Logs.d(TAG, "Callback onResponse: " + body);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
