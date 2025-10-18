package com.example.networktest.activity;

import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.networktest.R;
import com.example.networktest.api.RetrofitApi;
import com.example.networktest.util.RetrofitManager;
import com.example.networktest.vo.CommonItem;
import com.example.networktest.vo.PageVo;
import com.example.networktest.vo.PostStringVo;
import com.example.networktest.vo.TextVo;
import com.yitong.logs.Logs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Date：2025/10/16
 * Time：21:13
 * Author：chenshengrui
 */
public class RetrofitActivity extends AppCompatActivity {
    private static final String TAG = "RetrofitActivity";
    private static final String BASE_URL = "http://192.168.1.9:9102";

    Button btn;
    Button btn2;
    Button btn3;
    Button btn4;
    Button btn5;
    Button btn6;
    Button btn7;
    Button btn8;
    Button btn9;
    Button btn10;
    Button btn11;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        btn = findViewById(R.id.btn_retrofit_get);
        btn.setOnClickListener(v -> {
            retrofitGet();
        });
        btn2 = findViewById(R.id.btn_retrofit_page);
        btn2.setOnClickListener(v -> {
            retrofitPage();
        });
        btn3 = findViewById(R.id.btn_retrofit_page_map);
        btn3.setOnClickListener(v -> {
            retrofitPageMap();
        });
        btn4 = findViewById(R.id.btn_retrofit_post_string);
        btn4.setOnClickListener(v -> {
            retrofitPostString();
        });
        btn5 = findViewById(R.id.btn_retrofit_post_string_url);
        btn5.setOnClickListener(v -> {
            retrofitPostStringUrl();
        });
        btn6 = findViewById(R.id.btn_retrofit_post_body);
        btn6.setOnClickListener(v -> {
            retrofitPostBody();
        });
        btn7 = findViewById(R.id.btn_retrofit_post_file);
        btn7.setOnClickListener(v -> {
            retrofitPostFile();
        });
        btn8 = findViewById(R.id.btn_retrofit_post_files);
        btn8.setOnClickListener(v -> {
            retrofitPostFiles();
        });
        btn9 = findViewById(R.id.btn_retrofit_post_file_params);
        btn9.setOnClickListener(v -> {
            retrofitPostFileParams();
        });
        btn10 = findViewById(R.id.btn_retrofit_post_filed);
        btn10.setOnClickListener(v -> {
            retrofitPostFiled();
        });
        btn11 = findViewById(R.id.btn_retrofit_download_filed);
        btn11.setOnClickListener(v -> {
            retrofitDownloadFiled();
        });

    }

    private void retrofitDownloadFiled() {
        String downUrl = "download/10";
        Call<ResponseBody> task = RetrofitManager.create(RetrofitApi.class).downFile(downUrl);
        task.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Logs.d(TAG, "Thread: " + Thread.currentThread().getName());
                    Headers headers = response.headers();
                    for (int i = 0; i < headers.size(); i++) {
                        String name = headers.name(i);
                        String value = headers.value(i);
                        Logs.d(TAG, "Callback onResponse: " + name + ":" + value);
                    }
                    new Thread(() -> {
                        InputStream inputStream = null;
                        FileOutputStream outputStream = null;
                        try {
                            String fileName = headers.get("Content-Disposition").replace("attachment; filename=", "");
                            File outFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
                            if (!outFile.getParentFile().exists()) {
                                outFile.getParentFile().mkdirs();
                            }
                            inputStream = response.body().byteStream();
                            outputStream = new FileOutputStream(outFile);
                            byte[] bytes = new byte[1024];
                            int len;
                            while ((len = inputStream.read(bytes)) != -1) {
                                outputStream.write(bytes, 0, len);
                            }
                            outputStream.flush();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                if (outputStream != null) {
                                    outputStream.close();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void retrofitPostFiled() {
        RetrofitApi retrofitApi = RetrofitManager.create(RetrofitApi.class);
//        Call<PostStringVo> task = retrofitApi.loginFiled("admin", "123456");
        Map<String, String> params = new HashMap<>();
        params.put("userName", "admin");
        params.put("password", "123456");
        //也可以用map
        Call<PostStringVo> task = retrofitApi.loginFiled(params);

        task.enqueue(new Callback<PostStringVo>() {
            @Override
            public void onResponse(Call<PostStringVo> call, Response<PostStringVo> response) {
                if (response.isSuccessful()) {
                    PostStringVo pageVo = response.body();
                    Logs.d(TAG, "PostStringVo: " + pageVo.toString());
                }
            }

            @Override
            public void onFailure(Call<PostStringVo> call, Throwable t) {
                Logs.e(TAG, "PostStringVo: " + t.toString());
            }
        });
    }

    private void retrofitPostFileParams() {
        RetrofitApi retrofitApi = RetrofitManager.create(RetrofitApi.class);
        File file = new File("/sdcard/Android/data/com.csr.myFirstApp/files/Pictures/11.png");
        Map<String, String> params = new HashMap<>();
        params.put("description", "这是一张展示图片");
        params.put("isFree", "true");
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        Map<String, String> heads = new HashMap<>();
        params.put("token", "jgkzkfhkzkj");
        params.put("client", "iPhone26");
        Call<PostStringVo> task = retrofitApi.postFileWithParams(part, params, heads);
        task.enqueue(new Callback<PostStringVo>() {
            @Override
            public void onResponse(Call<PostStringVo> call, Response<PostStringVo> response) {
                if (response.isSuccessful()) {
                    PostStringVo pageVo = response.body();
                    Logs.d(TAG, "PostStringVo: " + pageVo.toString());
                }
            }

            @Override
            public void onFailure(Call<PostStringVo> call, Throwable t) {
                Logs.e(TAG, "PostStringVo: " + t.toString());
            }
        });
    }

    private void retrofitPostFiles() {
        RetrofitApi retrofitApi = RetrofitManager.create(RetrofitApi.class);
        List<MultipartBody.Part> parts = new ArrayList<>();
        MultipartBody.Part part1 = createPart("/sdcard/Android/data/com.csr.myFirstApp/files/Pictures/10.png");
        MultipartBody.Part part2 = createPart("/sdcard/Android/data/com.csr.myFirstApp/files/Pictures/11.png");
        MultipartBody.Part part3 = createPart("/sdcard/Android/data/com.csr.myFirstApp/files/Pictures/15.png");
        parts.add(part1);
        parts.add(part2);
        parts.add(part3);
        Call<PostStringVo> task = retrofitApi.postFiles(parts);
        task.enqueue(new Callback<PostStringVo>() {
            @Override
            public void onResponse(Call<PostStringVo> call, Response<PostStringVo> response) {
                if (response.isSuccessful()) {
                    PostStringVo pageVo = response.body();
                    Logs.d(TAG, "PostStringVo: " + pageVo.toString());
                }
            }

            @Override
            public void onFailure(Call<PostStringVo> call, Throwable t) {
                Logs.e(TAG, "PostStringVo: " + t.toString());
            }
        });
    }

    private MultipartBody.Part createPart(String path) {
        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
        return MultipartBody.Part.createFormData("files", file.getName(), requestBody);
    }

    private void retrofitPostFile() {
        RetrofitApi retrofitApi = RetrofitManager.create(RetrofitApi.class);
        File file = new File("/sdcard/Android/data/com.csr.myFirstApp/files/Pictures/11.png");
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        Call<PostStringVo> task = retrofitApi.postFile(part);
        task.enqueue(new Callback<PostStringVo>() {
            @Override
            public void onResponse(Call<PostStringVo> call, Response<PostStringVo> response) {
                if (response.isSuccessful()) {
                    PostStringVo pageVo = response.body();
                    Logs.d(TAG, "PostStringVo: " + pageVo.toString());
                }
            }

            @Override
            public void onFailure(Call<PostStringVo> call, Throwable t) {
                Logs.e(TAG, "PostStringVo: " + t.toString());
            }
        });
    }

    private void retrofitPostBody() {
        RetrofitApi retrofitApi = RetrofitManager.create(RetrofitApi.class);
        Call<PostStringVo> task = retrofitApi.postBody(new CommonItem("2323", "今天吃汉堡包"));
        task.enqueue(new Callback<PostStringVo>() {
            @Override
            public void onResponse(Call<PostStringVo> call, Response<PostStringVo> response) {
                if (response.isSuccessful()) {
                    PostStringVo pageVo = response.body();
                    Logs.d(TAG, "CommonItem: " + pageVo.toString());
                }
            }

            @Override
            public void onFailure(Call<PostStringVo> call, Throwable t) {
                Logs.e(TAG, "PostStringVo: " + t.toString());
            }
        });
    }

    private void retrofitPostStringUrl() {
        RetrofitApi retrofitApi = RetrofitManager.create(RetrofitApi.class);
        String url = "/post/string?string=今晚吃澳洲大波龙，帝王蟹";
        Call<PostStringVo> task = retrofitApi.postUrl(url);
        task.enqueue(new Callback<PostStringVo>() {
            @Override
            public void onResponse(Call<PostStringVo> call, Response<PostStringVo> response) {
                if (response.isSuccessful()) {
                    PostStringVo pageVo = response.body();
                    Logs.d(TAG, "PostStringVo: " + pageVo.toString());
                }
            }

            @Override
            public void onFailure(Call<PostStringVo> call, Throwable t) {
                Logs.e(TAG, "PostStringVo: " + t.toString());
            }
        });
    }

    private void retrofitPostString() {
        RetrofitApi retrofitApi = RetrofitManager.create(RetrofitApi.class);
        Call<PostStringVo> task = retrofitApi.postString("今晚吃啥");
        task.enqueue(new Callback<PostStringVo>() {
            @Override
            public void onResponse(Call<PostStringVo> call, Response<PostStringVo> response) {
                if (response.isSuccessful()) {
                    PostStringVo pageVo = response.body();
                    Logs.d(TAG, "PostStringVo: " + pageVo.toString());
                }
            }

            @Override
            public void onFailure(Call<PostStringVo> call, Throwable t) {

            }
        });

    }

    private void retrofitPageMap() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
        HashMap<String, String> map = new HashMap<>();
        map.put("keyword", "title");
        map.put("page", "10");
        map.put("order", "1");
        Call<PageVo> task = retrofitApi.getPagesMap(map);
        task.enqueue(new Callback<PageVo>() {
            @Override
            public void onResponse(Call<PageVo> call, Response<PageVo> response) {
                if (response.isSuccessful()) {
                    PageVo pageVo = response.body();
                    Logs.d(TAG, "pageVo: " + pageVo.toString());
                }
            }

            @Override
            public void onFailure(Call<PageVo> call, Throwable t) {

            }
        });
    }

    private void retrofitPage() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
        Call<PageVo> task = retrofitApi.getPages("title", 10, "1");
        task.enqueue(new Callback<PageVo>() {
            @Override
            public void onResponse(Call<PageVo> call, Response<PageVo> response) {
                if (response.isSuccessful()) {
                    PageVo pageVo = response.body();
                    Logs.d(TAG, "pageVo: " + pageVo.toString());
                }
            }

            @Override
            public void onFailure(Call<PageVo> call, Throwable t) {

            }
        });
    }

    private void retrofitGet() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
        Call<TextVo> task = retrofitApi.getText();
        task.enqueue(new Callback<TextVo>() {
            @Override
            public void onResponse(Call<TextVo> call, Response<TextVo> response) {
                Logs.d(TAG, "onResponse thread ==>> " + Thread.currentThread().getName());
                TextVo textVo = response.body();
                Logs.d(TAG, "textVo: " + textVo);
            }

            @Override
            public void onFailure(Call<TextVo> call, Throwable t) {

            }
        });

    }
}
