package com.example.myfirstapp.ktstudy

/**
 * Date：2024/9/10
 * Time：11:44
 * Author：chenshengrui
 * 匿名内部类，也可是单例
 */
class Object_ {
}

fun main() {
    /*
    通过关键字object
    创建一个匿名内部类
    默认继承Any类即是java的Object类
     */
    val obj = object {
        var name: String = "我是匿名内部类"
        fun test() {
            println(name)
        }

        override fun toString(): String {
            return name
        }
    }
    obj.test()

    val obj2 = object : C("我是C"), B {
        override fun testC() {
            super.testC()
        }

        override fun testB() {
            println("我是继承了接口B的匿名内部类")
        }
    }
    obj2.testB()
    println(obj2.name)
    obj2.testC()

    //单例
    //不能通过构造函数创建
    //    val sigleton = Sigleton()//错误
    val sigleton = Sigleton
    sigleton.test()

    ObjectClass.Tool.test()
    ObjectClass.test()
}

open class C(var name: String) {

    open fun testC() {
        println("C方法打印$name")
    }

}

interface B {
    fun testB()
}

object Sigleton {
    var name: String = "我是单例"
    fun test() {
        println(name)
    }
}

//伴生对象
class ObjectClass {

    //伴生对象
    companion object Tool{
        fun test() {
            println("我是伴生对象")
        }
    }

}