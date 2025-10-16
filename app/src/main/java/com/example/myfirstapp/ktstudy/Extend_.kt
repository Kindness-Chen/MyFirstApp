package com.example.myfirstapp.ktstudy

/**
 * Date：2024/9/3
 * Time：17:20
 * Author：chenshengrui
 * 继承，想要该类是继承的，需要添加open
 */
class Extend_ {


}

//添加关键字继承open,属性也能被重写
open class Human(protected open var name: String, protected var age: Int) {
    init {
        //name可能会出现空指针的情况，
        //因为子类重写了name，导致父类的name没有初始化
        //这时init父类的时候，子类的name也没初始化，这时就会空指针
        println("name:$name,age:$age")
    }

    constructor(name: String) : this("", 0)

    open fun show() {
        println("name:$name,age:$age")
    }

    open fun run() {
        println("跑步")
    }
}

class Sport : Human("体育生", 18) {

    //重写属性
    override var name: String
        get() = super.name
        set(value) {
            super.name = value
        }

    override fun show() {
        super.show()
        println("子类name:$name,age:$age")
    }

    fun playSport() {
        println("踢足球")
    }
}

fun main() {
    //向上转型
    var humam: Human = Sport()
    humam.run()
    humam.show()
    Human("张三")
    //相当于java的instanceof
    if (humam is Sport) {
        humam.playSport()
    }
    //向下转型
    humam = (humam as Sport)
    humam.playSport()
}