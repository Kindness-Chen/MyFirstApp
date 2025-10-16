package com.example.myfirstapp.ktstudy

/**
 * Date：2024/9/5
 * Time：21:52
 * Author：chenshengrui
 * 协变和逆变
 */
class OutAndIn_ {
}

class Test<T>(var data: T)

fun main() {
    //int是number的子类
    var test: Test<Int> = Test(1)
    //添加关键字 out 是协变，相当于java 的 <? extends Number>，只能是Number的子类
    //向上转换
    var test2: Test<out Number> = test

    //Number是Any的父类
    var test3: Test<Any> = Test(1)
    //添加关键字 in 是逆变，相当于java 的 <? super Number>，只能是Number的父类
    //向下转换
    var test4: Test<in Number> = test3

}