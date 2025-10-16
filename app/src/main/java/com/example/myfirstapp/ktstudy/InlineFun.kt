package com.example.myfirstapp.ktstudy

/**
 * Date：2024/9/2
 * Time：10:57
 * Author：chenshengrui
 * 内联函数
 */

fun main() {
//    inlineFun()
//    inlineFun2 { println(it) }
//    inlineFun3(
//        {
//            println(it)
//            //内联函数不需要@标签就能调用return，因为是造搬逻辑代码.
//            //return完成之后，后面的代码不会执行
//            return
//            //return加标签只返回单前函数
////            return@inlineFun3
//        }, {
//            println(it)
//            return@inlineFun3 "inlineFun3 block2 非内联函数（高阶函数）"
//        })
    sumFunc(10, 20)
}

/**
 * 内联函数，关键字inline
 * 调用内联函数的时候，会直接调用函数中的代码
 * 即是直接调用println("内联函数")
 */
inline fun inlineFun() {
    println("内联函数")
}

inline fun inlineFun2(block: (String) -> Unit) {
    block("inlineFun2 block 内联函数（高阶函数）")
}

inline fun inlineFun3(block: (String) -> Unit, noinline block2: (String) -> String) {
    block("inlineFun3 block 内联函数（高阶函数）")
    val test = block2("inlineFun3 block2 非内联函数（高阶函数）")
    println(test)
}