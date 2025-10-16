package com.example.myfirstapp.inner;

/**
 * Date：2025/9/20
 * Time：22:26
 * Author：chenshengrui
 */
public class InnerClass {
    public static void main(String[] args) {
        //演示一遍
        Outer02 outer02 = new Outer02();
        outer02.m1();
        System.out.println("outer02 的 hashcode=" + outer02);
    }
}

class Outer02 {
    //外部类
    private int n1 = 100;

    private void m2() {
        System.out.println("Outer02 m2()");
    }//私有方法

    public void m1() {//方法
        //1.局部内部类是定义在外部类的局部位置,通常在方法
        //3.不能添加访问修饰符,但是可以使用final 修饰
        //4.作用域 : 仅仅在定义它的方法或代码块中
        final class Inner02 {//局部内部类(本质仍然是一个类)
            //2.可以直接访问外部类的所有成员，包含私有的
            private int n1 = 800;

            public void f1() {
                //5. 局部内部类可以直接访问外部类的成员，比如下面 外部类n1 和 m2()
                //7. 如果外部类和局部内部类的成员重名时，默认遵循就近原则，如果想访问外部类的成员，
                //使用 外部类名.this.成员）去访问
                // 老韩解读 Outer02.this 本质就是外部类的对象, 即哪个对象调用了m1,Outer02.this就是哪个对象
                System.out.println("n1=" + n1 + " 外部类的 n1=" + Outer02.this.n1);
                System.out.println("Outer02.this hashcode=" + Outer02.this);
                m2();
            }
        }
        //6. 外部类在方法中，可以创建Inner02对象，然后调用方法即可
        Inner02 inner02 = new Inner02();
        inner02.f1();
    }
}

class Outer04 { //外部类
    private int n1 = 10;//属性

    public void method() {//方法
//基于接口的匿名内部类
//老韩解读
//1.需求： 想使用IA接口,并创建对象
//2.传统方式，是写一个类，实现该接口，并创建对象
//3.老韩需求是 Tiger/Dog 类只是使用一次，后面再不使用
//4. 可以使用匿名内部类来简化开发
//5. tiger 的编译类型 ?IA
        //6. tiger 的运行类型 ? 就是匿名内部类 Outer04$1
 /*
我们看底层 会分配 类名 Outer04$1
class Outer04$1 implements IA {
 @Override
 public void cry() {
 System.out.println("老虎叫唤...");
 }
 }
 */
        //7. jdk 底层在创建匿名内部类 Outer04$1,立即马上就创建了 Outer04$1实例，并且把地址返回给 tiger
        //8. 匿名内部类使用一次，就不能再使用
        IA tiger = new IA() {
            @Override
            public void cry() {
                System.out.println("老虎叫唤...");
            }
        };
        System.out.println("tiger 的运行类型=" + tiger.getClass());
        tiger.cry();
        tiger.cry();
        tiger.cry();


//        IA tiger = new Tiger();
//        tiger.cry();

        //演示基于类的匿名内部类分析

        //1. father 编译类型 Father
        //2. father 运行类型 Outer04$2
        //3. 底层会创建匿名内部类
        /*
                class Outer04$2 extends Father {
            @Override
            public void test() {
                System.out.println("匿名内部类重写了 test 方法");
            }
        }
         */

        //4. 同时也直接返回了 匿名内部类 Outer04$2的对象
        // 5. 注意("jack") 参数列表会传递给 构造器
        Father father = new Father("jack") {
            @Override
            public void test() {
            }
        };
        father.test();
        System.out.println("匿名内部类重写了 test 方法");
        System.out.println("father 对象的运行类型=" + father.getClass());//Outer04$2
        //基于抽象类的匿名内部类
        Animal animal = new Animal() {
            @Override
            void eat() {
                System.out.println("小狗吃骨头...");
            }
        };
        animal.eat();
    }
}

interface IA {//接口

    public void cry();
}

//class Tiger implements IA {
//
//    @Override
//    public void cry() {
//        System.out.println("老虎叫唤...");
//    }
//}


//class Dog implements IA {
//    @Override
//    public void cry() {
//        System.out.println("小狗汪汪...");
//    }
//}


class Father {//类

    public Father(String name) {//构造器
        System.out.println("接收到 name=" + name);
    }

    public void test() {//方法
    }
}

abstract class Animal { //抽象类
    abstract void eat();
}
