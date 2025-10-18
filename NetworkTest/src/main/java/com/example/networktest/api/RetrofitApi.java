package com.example.networktest.api;


import com.example.networktest.vo.CommonItem;
import com.example.networktest.vo.PageVo;
import com.example.networktest.vo.PostStringVo;
import com.example.networktest.vo.TextVo;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Date：2025/10/16
 * Time：21:17
 * Author：chenshengrui
 */
public interface RetrofitApi {

    @GET("/get/text")
    Call<TextVo> getText();

    @GET("/get/param")
    Call<PageVo> getPages(@Query("keyword") String keyword, @Query("page") int page, @Query("order") String order);

    @GET("/get/param")
    Call<PageVo> getPagesMap(@QueryMap Map<String, String> params);

    @POST("/post/string")
    Call<PostStringVo> postString(@Query("string") String string);

    @POST
    Call<PostStringVo> postUrl(@Url String PostStringByUrl);

    @POST("/post/comment")
    Call<PostStringVo> postBody(@Body CommonItem commonItem);

    @Multipart
    @POST("/file/upload")
    Call<PostStringVo> postFile(@Part MultipartBody.Part part);

    @Headers({"toke:csrNb", "password:12346"})
    @Multipart
    @POST("/files/upload")
    Call<PostStringVo> postFiles(@Part List<MultipartBody.Part> parts);

    //附加参数
    @Multipart
    @POST("/file/params/upload")
    Call<PostStringVo> postFileWithParams(@Part MultipartBody.Part part, @PartMap Map<String, String> params, @HeaderMap Map<String, String> headers);

    @FormUrlEncoded
    @POST("/login")
    Call<PostStringVo> loginFiled(@Field("userName") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("/login")
    Call<PostStringVo> loginFiled(@FieldMap Map<String, String> params);

    @GET
    Call<ResponseBody> downFile(@Url String fileUrl);

}
