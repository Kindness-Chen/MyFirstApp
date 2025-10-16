package com.example.myfirstapp.collection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * Date：2025/9/19
 * Time：21:50
 * Author：chenshengrui
 */
public class ListTest {


    public static void main(String[] args) {
        List<Object> objects = new ArrayList<>();
        objects.add(1);
        objects.add("2");
        objects.remove(0);
        Iterator<Object> iterator = objects.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();

        }

        List<Object> objects1 = new Vector<>(8);
        objects1.add(1);

        LinkedList<Object> objects2 = new LinkedList<>();
        objects2.add(1);
        ;
        objects2.set(0, 1);

        Set<Object> set = new HashSet<>();
        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);
        set.add(5);
        Iterator<Object> iterator1 = set.iterator();
        while (iterator1.hasNext()) {
            Object next = iterator1.next();
            System.out.println(next);
        }

        HashSet hashSet = new HashSet();
        hashSet.add("java");//到此位置，第 1 次 add 分析完毕.
        hashSet.add("php");//到此位置，第 2 次 add 分析完毕
        hashSet.add("java");
        System.out.println("set=" + hashSet);

        test();

        A[] as = new A[2];

        D d = new D();
        d.bind();
        d.test2();
        
    }

    private static void test() {
//        test();
    }

    interface A {
        void test(String info);
    }

    interface B extends A {
        void test2();
    }

    abstract static class C implements B {
        String info = "我是抽象类里面返回的值";

        public void bind() {
            System.out.println("D调用绑定");
            test3(info);
        }

        public abstract void test3(String get3);
    }

    static class D extends C {
        @Override
        public void test(String info) {
            System.out.println("传的值为：" + info);
            System.out.println("D");
        }

        @Override
        public void test3(String get3) {
            System.out.println("get3=" + get3);
            System.out.println("抽象方法：实现绑定");
        }

        @Override
        public void test2() {
            System.out.println("D");
        }
    }


}
