package com.example.myfirstapp.ktstudy

/**
 * Date：2024/9/10
 * Time：11:34
 * Author：chenshengrui
 * 枚举类
 */
class Enum_ {
}

enum class Ligth(var color: String) : A {
    RED("红色") {
        override fun test() {
            println("红色")
        }
    },
    GREEN("绿色"),
    BLUE("蓝色"),
    YELLOW("黄色");

    override fun test() {
        println(color)
    }
}

fun main() {
    val ligth = Ligth.RED
    println(ligth.name)
    println(ligth.color)
    println(ligth.ordinal)
    ligth.test()
    when (ligth) {
        Ligth.RED -> {
            println("红色")
        }
        Ligth.GREEN -> {
            println("绿色")
        }
        Ligth.BLUE -> {
            println("蓝色")
        }
        Ligth.YELLOW -> {
            println("黄色")
        }
    }
}

interface A {
    fun test()
}