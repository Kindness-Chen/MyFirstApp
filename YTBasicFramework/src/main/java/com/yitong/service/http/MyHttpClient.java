//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yitong.service.http;

import com.yitong.http.AsyncLogUtil;
import com.yitong.http.ResponseHandlerInterface;
import com.yitong.http.YTCookieJar;
import com.yitong.http.YTHttpCallback;
import com.yitong.http.YTLoggingInterceptor;
import com.yitong.http.cookie.HasCookieStore;
import com.yitong.http.cookie.MemoryCookieStore;
import com.yitong.http.cookie.YTCookieStore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Headers.Builder;

public class MyHttpClient {
    private static final String LOG_TAG = "AsyncHttpClient";
    private static final String DEFAULT_CHARSET = "utf-8";
    private static final int DEFAULT_CONNECT_TIMEOUT = 10000;
    private static final int DEFAULT_WRITE_TIMEOUT = 10000;
    private static final int DEFAULT_READ_TIMEOUT = 30000;
    private OkHttpClient httpClient;
    private String requestCharset;
    private final Map<String, String> clientHeaderMap;
    private final Interceptor headerInterceptor;

    public MyHttpClient() {
        this(10000, 10000, 30000);
    }

    public MyHttpClient(int connectTime, int writeTime, int readTime) {
        this(connectTime, writeTime, readTime, new MemoryCookieStore());
    }

    public MyHttpClient(int connectTime, int writeTime, int readTime, YTCookieStore cookieStore) {
        this.requestCharset = "utf-8";
        this.headerInterceptor = new Interceptor() {
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Builder headersBuilder = originalRequest.headers().newBuilder();
                String header;
                if (MyHttpClient.this.clientHeaderMap != null && MyHttpClient.this.clientHeaderMap.size() > 0) {
                    for (Iterator var4 = MyHttpClient.this.clientHeaderMap.keySet().iterator(); var4.hasNext(); headersBuilder.add(header, (String) MyHttpClient.this.clientHeaderMap.get(header))) {
                        header = (String) var4.next();
                        String overwritten = headersBuilder.get(header);
                        if (overwritten != null) {
                            AsyncLogUtil.d("AsyncHttpClient", String.format("Headers were overwritten! (%s | %s) overwrites (%s | %s)", header, MyHttpClient.this.clientHeaderMap.get(header), header, overwritten));
                            headersBuilder.removeAll(header);
                        }
                    }
                }

                Request newRequest = originalRequest.newBuilder().headers(headersBuilder.build()).build();
                Response response = chain.proceed(newRequest);
                return response;
            }
        };
        this.clientHeaderMap = new HashMap();
        this.httpClient = (new OkHttpClient.Builder()).connectTimeout((long) connectTime, TimeUnit.MILLISECONDS).writeTimeout((long) writeTime, TimeUnit.MILLISECONDS).readTimeout((long) readTime, TimeUnit.MILLISECONDS).cookieJar(new YTCookieJar(cookieStore)).addInterceptor(this.headerInterceptor).retryOnConnectionFailure(false).addNetworkInterceptor(new YTLoggingInterceptor()).build();
    }

    public OkHttpClient getHttpClient() {
        return this.httpClient;
    }

    public void setCharset(String charset) {
        this.requestCharset = charset;
    }

    public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        if (sslSocketFactory != null) {
            this.httpClient = this.httpClient.newBuilder().sslSocketFactory(sslSocketFactory).build();
        }

    }

    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        if (hostnameVerifier != null) {
            this.httpClient = this.httpClient.newBuilder().hostnameVerifier(hostnameVerifier).build();
        }

    }

    public void setLoggingEnabled(boolean loggingEnabled) {
        AsyncLogUtil.setLoggingEnabled(loggingEnabled);
    }

    public void removeAllHeaders() {
        this.clientHeaderMap.clear();
    }

    public void addHeader(String header, String value) {
        this.clientHeaderMap.put(header, value);
    }

    public void removeHeader(String header) {
        this.clientHeaderMap.remove(header);
    }

    public YTCookieStore getCookieStore() {
        CookieJar cookieJar = this.httpClient.cookieJar();
        if (cookieJar instanceof HasCookieStore) {
            HasCookieStore cookieStore = (HasCookieStore) cookieJar;
            return cookieStore.getCookieStore();
        } else {
            return null;
        }
    }

    public void get(String url, ResponseHandlerInterface responseHandler) {
        Request request = (new Request.Builder()).url(url.toString()).build();
        this.httpClient.newCall(request).enqueue(new YTHttpCallback(request, responseHandler));
    }

    public void post(String url, String postbody, String contentType, ResponseHandlerInterface responseHandler) {
        MediaType mediaType = MediaType.parse(contentType + "; charset=" + this.requestCharset);
        Request request = (new Request.Builder()).url(url).post(RequestBody.create(mediaType, postbody)).build();
        this.httpClient.newCall(request).enqueue(new YTHttpCallback(request, responseHandler));
    }

    public Response execute(String url, String postbody, String contentType) throws IOException {
        MediaType mediaType = MediaType.parse(contentType + "; charset=" + this.requestCharset);
        Request request = (new Request.Builder()).url(url).post(RequestBody.create(mediaType, postbody)).build();
        return this.httpClient.newCall(request).execute();
    }

    public void cancelRequests() {
        this.httpClient.dispatcher().cancelAll();
    }
}
