package com.example.myfirstapp.ktstudy

/**
 * Date：2024/9/2
 * Time：14:50
 * Author：chenshengrui
 * 类的学习、构造函数
 */
class Student public constructor(var name: String = "", var age: Int = 0) { //主构造函数不能编写函数体
    //class ClassConstructor (var ame: String, var age: Int)
    //如果是public修饰可以直接省略constructor
    //    class ClassConstructor(name: String, age: Int) {
    //        var name: String = name
    //        var age: Int = age
    //    }

    var name2: String = "陈升锐"

    //使用init 块，可以给初始值，也可以不写，但必须写在主构造函数之后
    //编写主构造函数体
    init {
        println("init初始化代码块 $name2")
    }

    //次要构造函数,需调用主构造函数用this关键字
    constructor(name: String) : this(name, 18) {
        println("次要构造函数")
    }

    //必须给初始值
    var sex: String = ""
    var address: String? = null

    //延迟初始化,可以不给初始值，但是在使用的时候必须赋值
    // lateinit不能用于基础类型，只能用于引用类型
    // lateinit var idNo: Int 错误
    lateinit var idNo: String

    override fun toString(): String {
        return "Student(name='$name', age=$age, sex='$sex', address=$address, idNo='$idNo')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Student

        if (name != other.name) return false
        if (age != other.age) return false
        if (sex != other.sex) return false
        if (address != other.address) return false
        if (idNo != other.idNo) return false

        return true
    }

    //    override fun hashCode(): Int {
    //        var result = name.hashCode()
    //        result = 31 * result + age
    //        result = 31 * result + sex.hashCode()
    //        result = 31 * result + (address?.hashCode() ?: 0)
    //        result = 31 * result + idNo.hashCode()
    //        return result
    //    }

    fun hello(name: String) {
        //添加{}消除歧义
        //        println("大家好，我叫${this.name}，今年${age}岁")
        println("大家好，我叫${name}，今年${age}岁")
    }

    fun compareWith() {
        //对于 Int? 类型，因为 Kotlin 不会缓存所有的整数对象，
        //所以即使两个变量的值相同，它们的引用也可能不同。
        val a: Int? = 12345
        val b: Int? = 12345
        println(a === b)

        //对于 Int 类型，由于缓存机制，
        //两个相同的值通常会引用同一个对象。
        val a2: Int = 12345
        val b2: Int = 12345
        println(a2 === b2)

        //对于字符串，跟java一样，
        //字面量会被存储在字符串常量池中，导致相同的字面量引用相同的对象
        val a3: String? = "12345"
        val b3: String? = "12345"
        println(a3 === b3)

        val a4: String = "12345"
        val b4: String = "12345"
        println(a4 === b4)
    }

    //重载运算符
    operator fun plus(anther: Student): Int {
        return this.age + anther.age
    }

    operator fun compareTo(anther: Student): Int {
        return this.age - anther.age
    }

    operator fun component1(): Any = name
    operator fun component2(): Any = age

}

fun main() {
    val stu1 = Student("小明", 18)
    println(stu1.name)
    println(stu1.age)
    stu1.address = "广东珠海"
    stu1.idNo = "123456789"
    stu1.hello("发哥")
    stu1.compareWith()

    val stu2 = Student()
    stu2.name = "小明"
    stu2.age = 18
    stu2.address = "广东珠海"
    stu2.idNo = "123456789"
    println(stu2)
    // ===判断是否通同个对象
    println(stu1 === stu2)
    //判断值是否相等，重写类的equal方法
    println(stu1 == stu2)

    val stu3 = Student("小红")
    stu3.age = 18
    stu3.address = "广东湛江"
    stu3.idNo = "8888888"
    println(stu3)

}