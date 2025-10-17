package com.example.networktest.api;


import com.example.networktest.vo.TextVo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Date：2025/10/16
 * Time：21:17
 * Author：chenshengrui
 */
public interface RetrofitApi {

    @GET("/get/text")
    Call<TextVo> getText();

}
