package com.example.myfirstapp.ktstudy

/**
 * Date：2024/9/8
 * Time：16:34
 * Author：chenshengrui
 */
class Map_ {
}

class Student2 {
    constructor(name: String, age: Int) {
        this.name = name
        this.age = age
    }

    var name = ""
    var age = 0
    override fun toString(): String {
        return "Student2(name='$name', age=$age)"
    }


}

fun main() {
    val map: MutableMap<Int, Student2> = mutableMapOf(
        1001 to Student2("张三", 18),
        1002 to Student2("tom", 20)
    )
    println(map)

    //根据key获取值
    val student: Student2? = map[1001]
    println(student)

    //是否包含这个key
    map.contains(1003)

    //是否包含值
    map.containsValue(Student2("tom", 20))
    if (1001 in map) {
    }

    println(map.keys)
    println(map.values)

    //遍历map
    map.forEach { (k, v) ->
        println("$k-$v")
    }

    for (kv in map) {
        println("${kv.key}---${kv.value}")
    }

    for ((k, v) in map) {
        println("$k---${v}")
    }
}