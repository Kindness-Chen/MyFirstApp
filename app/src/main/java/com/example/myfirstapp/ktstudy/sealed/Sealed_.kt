package com.example.myfirstapp.ktstudy.sealed

/**
 * Date：2024/9/10
 * Time：15:32
 * Author：chenshengrui
 * 密封类，其他不同包不能继承，第三方不能继承
 */
class Sealed_ {
    val csr = csr()
    val daiLi: DaiLi = DaiLi(csr)

    fun test() {
        daiLi.buyCar()
    }
}

fun main() {
    val sealed_ = Sealed_()
    sealed_.test()
}

sealed class Human {}

//也可以密封
sealed class Student : Human() {}

//也可以不密封
class Sport : Human() {}

interface IStudent {
    fun show()
}