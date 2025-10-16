package com.example.myfirstapp.application;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.example.myfirstapp.R;
import com.example.myfirstapp.util.FileUtil;
import com.yitong.logs.Logs;
import com.yitong.utils.BitmapUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Chenshengrui on 2021/10/18.
 */

public class LogsHandler {

    private static final String TAG = "LogsHandler";

    private static final LogsHandler INSTANCE = new LogsHandler();

    @SuppressLint("SimpleDateFormat")
    public SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy/MM/dd");

    private LogsHandler() {
    }

    public static LogsHandler getInstance() {
        return INSTANCE;
    }

    public void saveLogInfo2File(Context context, String txt) {
        txt += "\r\n";
        String time = format.format(new Date());
        String fileName = "log_" + time + ".txt";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
//                File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Log");
                String dir = context.getExternalFilesDir(null).getAbsolutePath() + File.separator + "Log";
                File file = new File(dir);
                if (!file.exists() || !file.isDirectory()) {
                    file.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(new File(dir + File.separator + fileName), true);
                fos.write(txt.getBytes());
                fos.close();
                InputStream is = context.getResources().openRawResource(R.drawable.circle_blue);
                Bitmap bm = BitmapFactory.decodeStream(is);
                String imgName = "MyDuoLaAMeng.JPG";
                String imgPath = context.getExternalFilesDir(null).getAbsolutePath() + File.separator + "Image" + File.separator + imgName;
                FileUtil.createFile(imgPath);
                boolean success = false;
                success = BitmapUtil.saveBitmapForLocal(bm, imgPath);
                if (success) {
                    FileUtil.insert2Album(context, new File(imgPath), imgName);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
