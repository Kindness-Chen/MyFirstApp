package com.example.myfirstapp.ktstudy

/**
 * Date：2024/9/3
 * Time：10:14
 * Author：chenshengrui
 *
 * 运算符重载函数（operator）
 * 中缀函数infix
 * 解析声明
 */
class Operator_ {

}

fun main() {
    val stu1 = Student("小明", 16)
    val stu2 = Student("小红", 18)

    // Student类重载加号
    val sum = stu1 + stu2
    if (stu1 > stu2) {
        println("${stu1.name}的年龄大于${stu2.name}")
    } else if (stu1 < stu2) {
        println("${stu2.name}的年龄大于${stu1.name}")
    } else {
        println("${stu1.name}的年龄等于${stu2.name}")
    }
    println(sum)

    //解析函数,需要在student中添加component1、component2方法，也支持lambda 表达式
    val (a, b) = stu1
    println("$a $b")

    val block: (Student) -> Unit = { (a, b) ->
        println("$a $b")
    }
    block(stu2)

}