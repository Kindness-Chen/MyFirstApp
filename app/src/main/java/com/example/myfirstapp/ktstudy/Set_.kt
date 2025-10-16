package com.example.myfirstapp.ktstudy

import java.util.SortedSet

/**
 * Date：2024/9/8
 * Time：16:00
 * Author：chenshengrui
 *无序、唯一性的集合
 */
class Set_ {
}

fun main() {
    //mutableSetOf相当于java的LinkedHashSet
    val set: MutableSet<String> = mutableSetOf("AAA", "BBB", "AAA", "CCC")
    val set2: MutableSet<String> = mutableSetOf("AAA", "BBB", "DDD", "EEE")
    //只能插入最后一个
    set.add("DDD")
    //错误，无序集合
    //    set[0]
    println(set)

    //两个集合的并集，会剔除重复的元素
    println(set union set2)

    //两个集合交集
    println(set intersect set2)

    //两个集合的差集
    println(set subtract set2)

    val set3: Set<String> = setOf("AAA", "BBB", "AAA", "CCC")
    val set4: Set<String> = setOf("AAA", "BBB", "DDD", "EEE")
    //去除集合重复元素
    println(set3 + set4)

    //无序
    val set5: HashSet<String> = hashSetOf("AAAfff", "BB", "DDD", "EEEr")
    println(set5)

    val set6: SortedSet<String> = sortedSetOf(object : Comparator<String> {
        override fun compare(o1: String?, o2: String?): Int {
            return o2?.length?.minus(o1?.length!!) ?: 0
        }

    }, "AAAfff", "BB", "DDD", "EEEr")

    println(set6)

}