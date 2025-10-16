package com.example.myfirstapp.ktstudy

/**
 * Date：2024/9/5
 * Time：17:34
 * Author：chenshengrui
 *
 * apply高阶扩展函数的运用
 */
class apply_ {
}

class Cat {
    var name: String = ""
    var age: Int = 0

    fun show() {
        println("name:$name,age:$age")
    }
}

fun main() {
    var cat: Cat? = null
    cat = Cat()
    cat.apply {
        name = "小花"
        age = 2
        show()
    }
    cat.let {
        it.name
    }
    cat.also {
        it.name
    }
    with(cat) {
        this.name = "小花"
        return
    }

    test(cat)
}

fun test(cat: Cat?): Cat? = cat?.apply {
    name = "小花"
    age = 2
    show()

    val mutableNumbers = arrayListOf(1, 2, 3)

    val doubled = mutableNumbers.let { list ->
        list.add(4) // 修改了原 mutableNumbers 列表的内容！
        list.map { it * 2 } // 返回一个新列表
    }
}