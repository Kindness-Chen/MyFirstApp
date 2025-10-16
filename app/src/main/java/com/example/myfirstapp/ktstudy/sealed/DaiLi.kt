package com.example.myfirstapp.ktstudy.sealed

/**
 * Date：2025/9/8
 * Time：20:38
 * Author：chenshengrui
 */
class DaiLi public constructor(private val car: csr?) {
    fun buyCar() {
        val test = car?.let {
            println("购买成功")
            123
        } ?: run {
            this.car?.show()
            println("购买失败")
        }
    }
}