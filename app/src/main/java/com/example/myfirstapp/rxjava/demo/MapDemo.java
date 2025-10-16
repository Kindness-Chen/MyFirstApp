package com.example.myfirstapp.rxjava.demo;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Date：2025/9/14
 * Time：20:53
 * Author：chenshengrui
 */
public class MapDemo {

    public static void main(String[] args) {
        StudentModel.init();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                List<Student> students = StudentModel.students;
//                for (Student student : students) {
//                    List<Student.Course> courseList = student.getCourseList();
//                    for (Student.Course course : courseList) {
//                        System.out.println(course.getCourseName());
//                    }
//                }
//            }
//        }).start();

//        Disposable subscribe = Observable.fromIterable(StudentModel.students)
//                .map(new Function<Student, List<Student.Course>>() {
//                    @Override
//                    public List<Student.Course> apply(Student students) throws Exception {
//                        return students.getCourseList();
//                    }
//                }).subscribe(new Consumer<List<Student.Course>>() {
//                    @Override
//                    public void accept(List<Student.Course> courses) throws Exception {
//                        for (Student.Course course : courses) {
//                            System.out.println(course.getCourseName());
//                        }
//                    }
//                });

        Disposable subscribe1 = Observable.fromIterable(StudentModel.students)
                .flatMap(new Function<Student, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Student student) throws Exception {
                        return Observable.fromIterable(student.getCourseList());
                    }
                }).subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        System.out.println(o.toString());
                    }
                });
    }
}
