package com.example.networktest.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.networktest.R;
import com.example.networktest.adapter.TextVoAdapter;
import com.example.networktest.vo.TextVo;
import com.google.gson.Gson;
import com.yitong.logs.Logs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class NetworkActivity extends AppCompatActivity {

    private static final String TAG = "NetworkActivity";
    private Button mBtn;
    private TextView mText;
    private RecyclerView mRvText;
    private TextVoAdapter mAdapter;
    private ImageView mImgApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        mBtn = findViewById(R.id.button);
        mText = findViewById(R.id.textView2);
        mRvText = findViewById(R.id.rv_text);
        mImgApi = findViewById(R.id.img_api);

        mAdapter = new TextVoAdapter();
        mRvText.setLayoutManager(new LinearLayoutManager(this));
        mRvText.setHasFixedSize(true);
        mRvText.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.top = 10;
                outRect.bottom = 10;
            }
        });
        mRvText.setAdapter(mAdapter);


        Logs.e(TAG, "当前线程1==》》" + Thread.currentThread().getName());
        mBtn.setOnClickListener(v -> {
            loadJson();
            loadImage();
        });
    }

    private void loadJson() {
        new Thread(() -> {
            try {
                HttpURLConnection urlConnection = getHttpURLConnection("http://192.168.1.9:9102/get/text");
                //结果码
                int code = urlConnection.getResponseCode();
                if (code == 200) {
                    Map<String, List<String>> headerFields = urlConnection.getHeaderFields();
                    for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
                        Logs.i(TAG, entry.getKey() + " ==" + entry.getValue());
                    }
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
//                    while ((line = bufferedReader.readLine()) != null) {
//                        Logs.i(TAG, line);
//                    }
                    line = bufferedReader.readLine();
//                    Thread.sleep(1000);
                    Gson gson = new Gson();
                    TextVo textVo = gson.fromJson(line, TextVo.class);
                    updateUI(textVo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void updateUI(TextVo textVo) {
        runOnUiThread(() -> {
            Logs.e(TAG, "当前线程2==》》" + Thread.currentThread().getName());
            mText.setText(textVo.getMessage());
            mAdapter.setData(textVo.getData());
        });

        Logs.e(TAG, "当前线程3==》》" + Thread.currentThread().getName());
    }

    //加载图片改成java api形式
    private void loadImage() {
        new Thread(() -> {
            try {
                HttpURLConnection urlConnection = getHttpURLConnection("http://192.168.1.9:9102/imgs/6.png");
                InputStream inputStream = urlConnection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (bitmap != null) {
                    mImgApi.post(() -> mImgApi.setImageBitmap(bitmap));
                }

                //采样率图片
                BitmapFactory.Options options = new BitmapFactory.Options();
                //不加载到内存，只获取图片宽高
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_account_home, options);
                //拿到控件尺寸
                int width = mImgApi.getMeasuredWidth();
                int height = mImgApi.getMeasuredHeight();
                options.inSampleSize = calculateInSampleSize(options, width, height);
                options.inJustDecodeBounds = false;
                Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_account_home, options);
                mImgApi.post(() -> {
                    mImgApi.setImageBitmap(bitmap1);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int maxWidth, int maxHeight) {

        //这里其实是获取到默认的高度和宽度，也就是图片的实际高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;

        //默认采样率为1，也就是不变嘛。
        int inSampleSize = 1;


        //===============核心算法啦====================
        if (width > maxWidth || height > maxHeight) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) maxHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) maxWidth);
            }

            final float totalPixels = width * height;

            final float maxTotalPixels = maxWidth * maxHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > maxTotalPixels) {
                inSampleSize++;
            }
        }
        //=============核心算法end================
        return inSampleSize;
    }

    private static @NonNull HttpURLConnection getHttpURLConnection(String loadUrl) throws IOException {
        URL url = new URL(loadUrl);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(10000);
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
        urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        urlConnection.setRequestProperty("Accept", "*/*");
        urlConnection.connect();
        return urlConnection;
    }

}