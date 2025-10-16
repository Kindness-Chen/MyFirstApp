package com.example.myfirstapp.ktstudy

/**
 * Date：2024/8/29
 * Time：11:01
 * Author：chenshengrui
 */
object FirstStudy {
    //    companion object {
    //        const val a = 10
    //    }
    val test = 10

    fun test() {
        println("FirstStudy test")
    }

}

fun main() {
    for (i in 1..10) {
        println("hello word!" + i)
    }

    val list = listOf("a", "b", "c")
    //    val listIterator = list.listIterator()
    //    while (listIterator.hasNext()) {
    //        println(listIterator.next())
    //    }
    //    list.forEach {
    //        println(it)
    //    }

    var csr = 20
    when (csr) {
        in 10..30 -> {

        }
    }
    when (csr) {
        1, 20, 30 -> {

        }

        10 -> {

        }

        20 -> {

        }
    }

    val a: IntProgression = 10..20 step 2
    val b: IntRange = 10..20
    val c: IntProgression = 10 downTo 1
    val d: IntRange = 2 until 10

    //    for (i in a) {
    //        println(i)
    //    }
    //    for (i in c) {
    //        println(i)
    //    }
    //    for (i in d) {
    //        println(i)
    //    }
    for (i in list.indices) {
        //        if (i == 1) continue
        //        if (i == 1) break
        //        if (i == 1) return
        println(list.get(i))
    }

    csr@ for (i in 1..2) {
        for (j in 1..2) {
            //            if (j == 2) continue@csr
            //            if (j == 2) break@csr
            //            if (j == 2) return
            println("$i ==== $j")
        }
    }
    //
    //    var i = 10
    //    while (i > 0) {
    //        println(i)
    //        i /= 2
    //    }
    //    test()

    //函数赋值给变量
    val sum: (Int, Int) -> Int = ::sumFunc
    println("函数赋值给变量: " + sum(10, 20))

    //函数赋值给变量,没有返回值
    val sum2: (Int, Int) -> Unit = { sum1, sum2 ->
        println(sum1 + sum2)
    }

    //lambda表达式
    val sum3: (Int, Int) -> Int = { sum1, sum2 ->
        sum1 + sum2
    }

    //不需要使用则用_表示
    val sum4: (Int, Int) -> Int = { _, sum2 ->
        sum2
    }

    println(sum3(50, 20))

    sumHigh {
        //参数接受的是it即使18
        println(it)
        //返回10
        0
    }

    sumHigh(10, 20) {
        //参数接受的是it即使18
        println(it)
        //返回10
        1
    }

    sumHigh(10, { a, b ->
        //参数接受的是it即使18
        println(a)
        println(b)
        //返回10
        2
    }, 20)

    //函数赋值给变量（函数不能直接return，需加@标签）
    val noReturn: (Int, Int) -> Int = test@{ a, b ->
        if (a > b) return@test 0
        a + b
    }
    println(noReturn(30, 20))


}

fun test() {
    val csr: String = synchronized(FirstStudy::class.java) {
        "hello word!"
        98.toString()
    }
    println(csr)
}

fun sumFunc(one: Int, two: Int): Int {
    return one + two
}

//高阶函数(用函数作为形参传入)
fun sumHigh(func: (String) -> Int): Int {
    println(func("用函数作为形参传入"))
    return 0
}

//高阶函数(用函数作为形参传入)
fun sumHigh(a: Int, b: Int, func: (String) -> Int): Int {
    println(func("用函数作为形参传入(多个参数)"))
    return 2
}

//高阶函数(用函数作为形参传入)
fun sumHigh(a: Int, func: (String, String) -> Int, b: Int): Int {
    println(func("用函数作为形参传入(多个参数、函数作为形参放中间)", "sds"))
    return 2
}


