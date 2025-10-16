package com.example.myfirstapp.javatest;

import java.util.Scanner;

/**
 * Date：2025/9/15
 * Time：16:14
 * Author：chenshengrui
 */
public class JavaTest {

    public static void main(String[] args) {
        //java中，通过int关键字去定义一个整型的变量
        //然后去给我们的整数去命名，就好比给你的小宠物、或者小玩具命名一样，命名好后，这个变量就变成a啦
        //什么是整型呢？就好比我们数学学习中的正整数+负整数+0、阿拉伯数字
        //-3,-2,-1,0,1,2,3
        //赋值运算符=，赋值运算符的左边是变量，右边是值，就好像学校的数学 1+1 = 2，类似的意思
        int a = 1;//a赋值成1
        a = 2;//a赋值成2
        System.out.println("a = " + a);

        //请输入你的手机号码，这个是java的输入例子，换成python的input的会教学就会简单一点
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("请输入你的手机号码: ");
//        String input = scanner.nextLine(); // 等待用户输入并回车
//        a = Integer.parseInt(input);
//        System.out.println("你的手机号码是: " + a);
//        scanner.close();

        //加（+） 减（-） 乘（*） 除（/）
        int b = 1 + 1;
        System.out.println("b = " + b);
        int c = 1 - 1;
        System.out.println("c = " + c);
        int d = 1 * 1;
        System.out.println("d = " + d);
        int e = 1 / 1;
        System.out.println("e = " + e);
    }
}
