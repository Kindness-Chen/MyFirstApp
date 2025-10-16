package com.example.networktest.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.networktest.R;
import com.example.networktest.vo.CommonItem;
import com.google.gson.Gson;
import com.yitong.logs.Logs;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PostActivity extends AppCompatActivity {
    private static final String TAG = "PostActivity";

    Button mBtn;
    Button mBtn2;
    Button mBtn3;
    Button mBtn4;
    private static final String BASE_URL = "http://192.168.1.9:9102";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mBtn = findViewById(R.id.button2);
        mBtn.setOnClickListener(v -> {
            post();
        });
        mBtn2 = findViewById(R.id.button3);
        mBtn2.setOnClickListener(v -> {
            getParamsMethod();
        });

        mBtn3 = findViewById(R.id.button4);
        mBtn3.setOnClickListener(v -> {
            OutputStream outputStream = null;
            try {
                File fileOne = new File("/storage/emulated/0/Download/1.jpg");
                File fileTwo = new File("/storage/emulated/0/Download/rBsADV2rEz-AIzSoAABi-6nfiqs456.png");
                File fileThree = new File("/storage/emulated/0/Download/2.jpg");
                File fileFour = new File("/storage/emulated/0/Download/rBPLFV2A8POASi1aAAE-PgvGzOo723.jpg");
                String fileKey = "files";
                String fileType = "image/jpeg";
                String BOUNDARY = "--------------------------954555323792164398227139";
                //String BOUNDARY = "----------------------------954555323792164398227139--";
                //String BOUNDARY = "----------------------------954555323792164398227139";
                URL url = new URL(BASE_URL + "/files/upload");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setRequestProperty("User-Agent", "Android/" + Build.VERSION.SDK_INT);
                httpURLConnection.setRequestProperty("Accept", "*/*");
                httpURLConnection.setRequestProperty("Cache-Control", "no-cache");
                httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
                httpURLConnection.setRequestProperty("Connection", "keep-alive");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //连接
                httpURLConnection.connect();
                outputStream = httpURLConnection.getOutputStream();
                uploadFile(fileOne, fileKey, fileOne.getName(), fileType, BOUNDARY, outputStream, false);
                uploadFile(fileTwo, fileKey, fileTwo.getName(), fileType, BOUNDARY, outputStream, false);
                uploadFile(fileFour, fileKey, fileFour.getName(), fileType, BOUNDARY, outputStream, false);
                uploadFile(fileThree, fileKey, fileThree.getName(), fileType, BOUNDARY, outputStream, true);
                outputStream.flush();
                //获取返回的结果
                int responseCode = httpURLConnection.getResponseCode();
                Logs.d(TAG, "responseCode -- > " + responseCode);
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
                    String result = bf.readLine();
                    Log.d(TAG, "result -- > " + result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mBtn4 = findViewById(R.id.button5);
        mBtn4.setOnClickListener(v -> {
            downloadFile();
        });
    }

    private void uploadFile(File file,
                            String fileKey,
                            String fileName,
                            String fileType,
                            String BOUNDARY,
                            OutputStream outputStream,
                            boolean isLast) throws Exception {
        //准备数据
        StringBuilder headerSbInfo = new StringBuilder();
        headerSbInfo.append("--");
        headerSbInfo.append(BOUNDARY);
        headerSbInfo.append("\r\n");
        headerSbInfo.append("Content-Disposition: form-data; name=\"" + fileKey + "\"; filename=\"" + fileName + "\"");
        headerSbInfo.append("\r\n");
        headerSbInfo.append("Content-Type: " + fileType);
        headerSbInfo.append("\r\n");
        headerSbInfo.append("\r\n");
        byte[] headerInfoBytes = headerSbInfo.toString().getBytes("UTF-8");
        outputStream.write(headerInfoBytes);
        //文件内容
        FileInputStream fos = new FileInputStream(file);
        BufferedInputStream bfi = new BufferedInputStream(fos);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = bfi.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        //写尾部信息
        StringBuilder footerSbInfo = new StringBuilder();
        footerSbInfo.append("\r\n");
        footerSbInfo.append("--");
        footerSbInfo.append(BOUNDARY);
        if (isLast) {
            footerSbInfo.append("--");
            footerSbInfo.append("\r\n");
        }
        footerSbInfo.append("\r\n");
        outputStream.write(footerSbInfo.toString().getBytes("UTF-8"));
    }

    private void post() {
        new Thread(() -> {
            OutputStream outputStream = null;
            InputStream inputStream = null;
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL("http://192.168.1.9:9102/post/comment");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
                urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");

                CommonItem hello = new CommonItem("1", "hello");
                Gson gson = new Gson();
                String json = gson.toJson(hello);
                byte[] bytes = json.getBytes();
                urlConnection.setRequestProperty("Content-Length", String.valueOf(bytes.length));
                urlConnection.connect();

                outputStream = urlConnection.getOutputStream();
                outputStream.write(bytes);
                outputStream.flush();

                int code = urlConnection.getResponseCode();
                if (code == 200) {
                    inputStream = urlConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    Logs.i("PostActivity", bufferedReader.readLine());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getParamsMethod() {
        new Thread(() -> {
            OutputStream outputStream = null;
            InputStream inputStream = null;
            BufferedReader bufferedReader = null;
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("keyword", "1");
                params.put("page", "hello");
                params.put("order", "chenshengrui");

                StringBuilder sb = new StringBuilder("?");
                Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> next = iterator.next();
                    sb.append(next.getKey());
                    sb.append("=");
                    sb.append(next.getValue());
                    if (iterator.hasNext()) {
                        sb.append("&");
                    }
                }
                Logs.i("PostActivity", sb.toString());
                URL url = new URL("http://192.168.1.9:9102/get/param" + sb);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
                urlConnection.setRequestProperty("Accept", "*/*");
                urlConnection.connect();

                int code = urlConnection.getResponseCode();
                if (code == HttpURLConnection.HTTP_OK) {
                    inputStream = urlConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    Logs.i("PostActivity", bufferedReader.readLine());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void downloadFile() {
        new Thread(() -> {
            FileOutputStream fileOutputStream = null;
            InputStream inputStream = null;
            try {
                URL url = new URL(BASE_URL + "/download/10");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
                urlConnection.setRequestProperty("Accept", "*/*");
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
                    for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
                        Logs.d(TAG, entry.getKey() + ":" + entry.getValue());
                    }
                    String headerField = urlConnection.getHeaderField("Content-Disposition");
                    String fileName = headerField.replace("attachment; filename=", "");

                    File externalFilesDir = this.getBaseContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    if (null != externalFilesDir && !externalFilesDir.exists()) {
                        externalFilesDir.mkdirs();
                    }
                    File file = new File(externalFilesDir, fileName);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    fileOutputStream = new FileOutputStream(file);
                    inputStream = urlConnection.getInputStream();
                    byte[] bytes = new byte[1024];
                    while (inputStream.read(bytes, 0, bytes.length) != -1) {
                        fileOutputStream.write(bytes);
                    }
                    fileOutputStream.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
