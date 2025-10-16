package com.example.myfirstapp.ktstudy

/**
 * Date：2024/9/8
 * Time：13:53
 * Author：chenshengrui
 */
class Array_ {
}

fun main() {
    val array: Array<Int> = arrayOf(9, 4, 6, 7, 3)

    //循环遍历数组
    //array.indices返回数组下标
    for (i in array.indices) {
        println(array[i])
    }

    //返回数组元素
    for (item in array) {
        println(item)
    }

    for (i in 0 until array.size) {
        println(array[i])
    }

    //返回下标和元素
    for ((i, item) in array.withIndex()) {
        println("$i ===$item")
    }

    //高阶函数返回元素
    array.forEach {
        println(it)
    }

    array.forEachIndexed { index, item ->
        println("$index ==$item")
    }

    //kotlin提供效率高的原生数组
    val intArray: IntArray = intArrayOf(1, 2, 3)
    val intArray2: Array<Int> = arrayOf(1, 2, 3)
    //正确
    test(*intArray)
    //错误
    //    test(*intArray2)

    //二维数组
    val array2: Array<IntArray> = arrayOf(intArrayOf(1, 2), intArrayOf(3, 4))
    for (item in array2) {
        for (i in item) {
            println(i)
        }
    }

    //数组转集合
    array2.toList()
    array2.toSet()
    array2.toMutableList()
    array2.toMutableSet()
}

//可变长度参数vararg
fun test(vararg ints: Int) {
    var intArray1: IntArray = ints
}