package com.example.myfirstapp.activity.ndk;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Date：2025/10/5
 * Time：16:32
 * Author：chenshengrui
 */
public class MyNDKActivity2 extends AppCompatActivity {

    public static int age = 18;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stringFromJNI5();
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            System.out.println("修改后的" + age);
        }, 1000);

        doMethod();

    }

    public static native void stringFromJNI5();


    public native void doMethod();

    //C++调用
    public int add(int a, int b) {
        System.out.println("C++调用");
        return a + b;
    }

    //C++调用
    public String getInfo(String name, int age) {
        System.out.println("个人信息姓名为：" + name + "，年龄为：" + age);
        return "获取信息成功";
    }
}
