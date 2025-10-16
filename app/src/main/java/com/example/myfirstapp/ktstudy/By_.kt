package com.example.myfirstapp.ktstudy

import kotlin.properties.Delegates

/**
 * Date：2024/9/10
 * Time：14:33
 * Author：chenshengrui\
 *
 * 委托模式，通过关键字by
 */
class By_ {
}

interface Base {
    fun print()
}

class BaseImpl() : Base {
    override fun print() {
        println("BaseImpl print")
    }
}

//委托给Base
class Delegate(val base: Base) : Base {
    override fun print() = base.print()
}

//也可写成，通过关键字by实现
class Delegate2(base: Base) : Base by base

fun main() {
    val delegate = Delegate(BaseImpl())
    delegate.print()

    val delegate2 = Delegate2(BaseImpl())
    delegate2.print()

    val example = Example("csr")
    println(example.p)
    example.p2 = "我变化了1"
    example.p2 = "我变化了2"
    //默认调用了a的get方法
    println(example.p3)
    //p3与name相互绑定了，所以name也会变化
    example.p3 = "BBB"
    println(example.name)

    //将name和age委托给map
    val map = mutableMapOf<String, Any>(
        "name" to "csr",
        "age" to 18,
    )
    map.put("ldh", 48)
    val user = User(map)
    map["age"] = 19
    println(user)
    println(map)

}

class Example(var name: String) {
    //由于lazy没有set方法，只能用val
    //lazy只有在使用p的时候才开始加载
    val p: String by lazy { "委托给lazy" }

    //可以委托给观察者，实时观察p的值变化
    var p2: String by Delegates.observable("初始值") { property, oldValue, newValue ->
        println("property===${property.name}\noldValue===$oldValue\nnewValue===$newValue")
    }

    //也可以委托给属性
    var p3: String by ::name
}

class User(map: MutableMap<String, Any>) {
    var name: String by map
    var age: Int by map
    override fun toString(): String {
        return "名称：$name, 年龄：$age"
    }
}