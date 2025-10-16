package com.example.myfirstapp.ktstudy

/**
 * Date：2024/9/5
 * Time：14:53
 * Author：chenshengrui
 *
 * 类的扩展函数
 */
class SpreadFunction {
}

class Dog {
    fun cry() {
        println("wangwang")
    }
}

/**
 * 扩展函数,扩展了run()方法
 */
fun Dog.run() {
    println("跑步")
}

fun String.test() {
    println("test")
}

fun main() {
    var dog = Dog()
    dog.cry()
    dog.run()

    var string = String()
    string.test()

    //String类型的扩展函数
    val func: String.() -> Int = {
        this.length
    }
    println("csr".func())
    println(func("csr666"))
}