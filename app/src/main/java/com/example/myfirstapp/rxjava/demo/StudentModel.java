package com.example.myfirstapp.rxjava.demo;

import java.util.ArrayList;
import java.util.List;

/**
 * Date：2025/9/14
 * Time：20:48
 * Author：chenshengrui
 */
public class StudentModel {

    static List<Student> students;

    public static void init() {
        students = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Student student = new Student();
            List<Student.Course> courseList = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                Student.Course course = new Student.Course();
                course.setCourseName("课程" + j);
                courseList.add(course);
            }
            student.setCourseList(courseList);
            student.setName("学生" + i);
            students.add(student);
        }
    }
}
