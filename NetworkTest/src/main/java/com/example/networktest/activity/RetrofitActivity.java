package com.example.networktest.activity;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.networktest.R;
import com.example.networktest.api.RetrofitApi;
import com.example.networktest.vo.TextVo;
import com.yitong.logs.Logs;

import java.io.IOException;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        btn = findViewById(R.id.btn_retrofit_get);
        btn.setOnClickListener(v -> {
            retrofitGet();
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
