package com.example.myfirstapp.ktstudy

/**
 * Date：2024/9/8
 * Time：20:58
 * Author：chenshengrui
 *
 * data类必须要有主构造函数，且属性必须有val或者var
 * 编译器自动生成equals()、hashCode()、toString()、componentN()、copy()等方法
 */
data class DataClass(var name: String, var age: Int) {
    //写在里面的属性，不会生成默认的getter和setter方法以及equals()、hashCode()、toString()等方法
    var address: String = ""
}

fun main() {
    val dataClass = DataClass("chen", 18)
    //生成的copy()方法会拷贝一个一样的对象
    val copyDataClass = dataClass.copy()
    println(copyDataClass)
    println(dataClass == copyDataClass)
    println(dataClass === copyDataClass)
}