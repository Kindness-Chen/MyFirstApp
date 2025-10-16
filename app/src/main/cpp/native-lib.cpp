#include <jni.h>
#include <string>
#include <iostream>
#include <android/log.h>

#define TAG "MyNDK"
#define LOG(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__);

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_myfirstapp_activity_ndk_MyNDKActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


extern "C" // 下面的代码，采用C的编译方式? 为什么要这样，后面专门讲JNIEnv源码
JNIEXPORT // JNIEXPDRT:JN重要标记关键字，不能少(VS编译能通过，运行会报错) / (AS 运行不会报错)，规则(标记为该方法可以被外部调用)
jstring //void 代表java中的 void, jstring代表java中的 String
JNICALL // 也是一个关键字，(可以少的) jni call(约束函数入栈顺序，和堆栈内存清理的规则)
//Java 包名 com_xx_xx_1_类名_方法名
//JNIEnv *enV JNI 桥梁 核心
// jobject == MainActivity this 实例调用的
//iclass == MainActivity class 类调用的
Java_com_example_myfirstapp_activity_ndk_MyNDKActivity_stringFromJNI2(
        JNIEnv *env,
        jobject /* this */) {
    //如果当前是 native-lib.c （C语言）
    //(*env)->xxx函数
    //(*env)->DeleteLocalRef()
    //C语言是JNIEnv *env 级指针
//    (*env).DeleteLocalRef(env,NULL);//C是没有对象的，想持有env坏境，就必须传递进去
    //如果当前是 native-lib.cpp->调用一级指针下的函数//env->xxx函数（C++）
    //env->DeleteLocalRef()
    //C++语言是JNIEnv *env 级指针
//    env->DeleteLocalRef(NULL);//C++是有对象的，本来就会持有this，所以不需要传

    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_myfirstapp_activity_ndk_MyNDKActivity_stringFromJNI3(
        JNIEnv *env,
        jobject myNDKActivityThis) {

    jclass MyNDKActivityCls = env->FindClass("com/example/myfirstapp/activity/ndk/MyNDKActivity");
    jfieldID myNameField = env->GetFieldID(
            MyNDKActivityCls, "name", "Ljava/lang/String;");//"L;"是格式符，Ljava/lang/String;
//    jfieldID myNameField = env->GetFieldID(MyNDKActivityCls, "myName", "Ljava/lang/String;");
    //String Student Person Object 引用类型 == JNI全部命名为 Object
//    void SetObjectField(jobject obj, jfieldID fieldID, jobject value)
    jstring myName = env->NewStringUTF("修改为：shengrui");
//    env->SetObjectField(myNDKActivityThis, myNameField, "修改为：shengrui");//直接修改会崩溃，需要通过JNI去转换
    env->SetObjectField(myNDKActivityThis, myNameField, myName);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_myfirstapp_activity_ndk_MyNDKActivityKt_stringFromJNI4(JNIEnv *env,
                                                                        jclass myNDKActivityCls) {
//    jclass MyNDKActivityCls = env->GetObjectClass(thiz);
    jfieldID ageFiledId = env->GetStaticFieldID(myNDKActivityCls, "mAge", "I");
    env->SetStaticIntField(myNDKActivityCls, ageFiledId, 19);

    std::cout << "mAge:" << env->GetStaticIntField(myNDKActivityCls, ageFiledId) << std::endl;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_myfirstapp_activity_ndk_MyNDKActivity2_stringFromJNI5(JNIEnv *env, jclass clazz) {
    // TODO: implement stringFromJNI5()
    jfieldID ageFiledId = env->GetStaticFieldID(clazz, "age", "I");
    int age = env->GetStaticIntField(clazz, ageFiledId);
    // jint 背后就是int，所以可以直接用:但是string 必须用 jstning
    env->SetStaticIntField(clazz, ageFiledId, age + 100);
    LOGE("%s", "mAge:")
}

extern "C"
JNIEXPORT void JNICALL
Java_com_example_myfirstapp_activity_ndk_MyNDKActivity_stringFromJNI4(JNIEnv *env,
                                                                      jclass myNDKActivityCls) {
    jfieldID ageFiledId = env->GetStaticFieldID(myNDKActivityCls, "mAge", "I");
    // jint 背后就是int，所以可以直接用:但是string 必须用 jstning
    env->SetStaticIntField(myNDKActivityCls, ageFiledId, 19);
    int result = env->GetStaticIntField(myNDKActivityCls, ageFiledId);
    LOGE("mAge:%d", result)
    std::cout << "mAge:" << env->GetStaticIntField(myNDKActivityCls, ageFiledId) << std::endl;
}
extern "C"
JNIEXPORT void JNICALL
Java_com_example_myfirstapp_activity_ndk_MyNDKActivity2_doMethod(JNIEnv *env, jobject thiz) {

    jclass myNDKActivityCls = env->GetObjectClass(thiz);
    //jmethodID GetMethodID(jclass clazz, const char* name, const char* sig)
    jmethodID addMethodId = env->GetMethodID(myNDKActivityCls, "add", "(II)I");

    //void CallVoidMethod(jobject obj, jmethodID methodID, ...)
    int result = env->CallIntMethod(thiz, addMethodId, 1, 2);


    //===============================================================================================
    jmethodID getInfoMethodId = env->GetMethodID(myNDKActivityCls, "getInfo",
                                                 "(Ljava/lang/String;I)Ljava/lang/String;");
    jstring name = env->NewStringUTF("shengrui");
    jstring info = (jstring) env->CallObjectMethod(thiz, getInfoMethodId, name, 18);
    char *infoStr = (char *) env->GetStringUTFChars(info, NULL);
    LOGE("info:%s", infoStr)
}