package com.example.myfirstapp.ktstudy

/**
 * Date：2024/9/8
 * Time：15:50
 * Author：chenshengrui
 */
class List_ {
}

fun main() {
    //可变数组
    val list: MutableList<String> = mutableListOf("AAA", "BBB", "CCC")
    list.add("DDD")
    list.add(1, "EEE")
    list.forEach { println(it) }

    //不可变数组，只读
    val list2: List<String> = listOf("AAA", "BBB", "CCC")
    list2.forEach { println(it) }

    //映射，将集合元素转换为另一个集合
    println(list2.map { it.length })
    println(list2.mapIndexed { index, s -> "$index.$s" })

    //压缩两个集合，成为map
    val list3: List<Int> = listOf(1, 2, 3)
    val pairs: List<Pair<Int, String>> = list3.zip(list2)
    println(pairs)

    //解压，拆开
    val unzip: Pair<List<Int>, List<String>> = pairs.unzip()
    //左边
    println(unzip.first)
    //右边
    println(unzip.second)

    //快速构建map，associateWith,作为key
    val associateWith: Map<Int, Int> = list3.associateWith { it * 2 }
    println(associateWith)

    //快速构建map，associateWith,作为value
    val associateBy: Map<Int, Int> = list3.associateBy { it }
    println(associateBy)

    //快速构建map，associate，自己封装
    val associate: Map<Int, Int> = list3.associate { it to it * 2 }
    println(associate)

    //将两个集合合并成一个
    val list4 = listOf(listOf(1, 2, 3), listOf(1, 2, 3))
    val flatten = list4.flatten()
    println(flatten)

    val list5 = listOf(Content(listOf(1, 2, 3)), Content(listOf(1, 2, 3)))
    val flatMap = list5.flatMap { it.list }
    println(flatMap)

    //过滤不满足的条件
    val filter = list3.filter { it % 2 == 0 }
    println(filter)

    //数据量大的话可以使用序列来完成asSequence
    val filterNot = list3.asSequence().filterNot { it % 2 == 0 }


}

class Content(var list: List<Int>)