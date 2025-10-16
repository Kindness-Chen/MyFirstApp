package com.example.myfirstapp.activity.ndk

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myfirstapp.R

/**
 * Date：2025/10/4
 * Time：21:23
 * Author：chenshengrui
 */



open class MyNDKActivity : MyNDKActivity2() {

    var name: String = "chenshengrui";

    var text: TextView? = null
    var textAge: TextView? = null

    companion object {
        init {
            System.loadLibrary("myfirstapp")
        }

        //静态方法
        @JvmStatic
        external fun stringFromJNI4()

        //静态属性
        @JvmField
        var mAge: Int = 13
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_my_ndk)
        text = findViewById(R.id.textView)
        textAge = findViewById(R.id.textViewAge)
        text?.text = stringFromJNI()
        stringFromJNI3()
        text?.text = name
        //调用父类的age
        textAge?.text = mAge.toString()
        stringFromJNI4()
        println("stringFromJNI")

    }

    external fun stringFromJNI(): String

    external fun stringFromJNI3()

}