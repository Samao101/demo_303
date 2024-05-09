package com.example.demo.config.dataSeeder;

import java.io.IOException;
import java.util.Set;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.example.demo.config.dataLoader.AdminDataLoader;
import com.example.demo.config.dataLoader.CourseDataLoader;
import com.example.demo.config.dataLoader.StudentDataLoader;
import com.example.demo.config.dataLoader.TeacherDataLoader;
import com.example.demo.models.DTOs.request.Admin.CreateAdminRequest;
import com.example.demo.models.DTOs.request.Course.BaseCourseRequest;
import com.example.demo.models.DTOs.request.Student.CreateStudentRequest;
import com.example.demo.models.DTOs.request.Teacher.CreateTeacherRequest;
import com.example.demo.service.AdminService;
import com.example.demo.service.CourseService;
import com.example.demo.service.TeacherService;
import com.example.demo.service.StudentService;

@Component
public class DatabaseSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final CourseService courseService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final AdminService adminService;

    private final CourseDataLoader courseDataLoader;
    private final StudentDataLoader studentDataLoader;
    private final TeacherDataLoader teacherDataLoader;
    private final AdminDataLoader adminDataLoader;


    public DatabaseSeeder(  CourseService courseService, 
                            StudentService studentService, 
                            TeacherService teacherService, 
                            AdminService adminService,
                            CourseDataLoader courseDataLoader,
                            StudentDataLoader studentDataLoader,
                            AdminDataLoader adminDataLoader, 
                            TeacherDataLoader teacherDataLoader) {
        this.courseService = courseService;
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.adminService = adminService;

        this.courseDataLoader = courseDataLoader;
        this.adminDataLoader = adminDataLoader;
        this.teacherDataLoader = teacherDataLoader;
        this.studentDataLoader = studentDataLoader;
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        seedCourses();
        seedStudents();
        seedTeachers();
        seedAdmins();
    }

    private void seedCourses() {
        try {
            Set<BaseCourseRequest> courses = courseDataLoader.loadCourseData();
            courses.forEach(courseDto -> {
                try {
                    courseService.createCourse(courseDto);
                    System.out.println("Successfully seeded course: " + courseDto.getCourseName());
                } catch (Exception e) {
                    System.err.println("Failed to seed course " + courseDto.getCourseName() + ": " + e.getMessage());
                }
            });
        } catch (IOException e) {
            System.err.println("Failed to load course data from JSON: " + e.getMessage());
        }
    }

    private void seedStudents() {
        try {
            Set<CreateStudentRequest> students = studentDataLoader.loadStudentData();
            students.forEach(studentDto -> {
                try {
                    studentService.createStudent(studentDto);
                    System.out.println("Successfully seeded student: " + studentDto.getName());
                } catch (Exception e) {
                    System.err.println("Failed to seed student " + studentDto.getName() + ": " + e.getMessage());
                }
            });
        } catch (IOException e) {
            System.err.println("Failed to load student data from JSON: " + e.getMessage());
        }
    }

    private void seedTeachers() {
        try {
            Set<CreateTeacherRequest> teachers = teacherDataLoader.loadTeacherData();
            teachers.forEach(teacherDto -> {
                try {
                    teacherService.createTeacher(teacherDto);
                    System.out.println("Successfully seeded teacher: " + teacherDto.getName());
                } catch (Exception e) {
                    System.err.println("Failed to seed teacher " + teacherDto.getName() + ": " + e.getMessage());
                }
            });
        } catch (IOException e) {
            System.err.println("Failed to load teacher data from JSON: " + e.getMessage());
        }
    }

    private void seedAdmins() {
        try {
            Set<CreateAdminRequest> admins = adminDataLoader.loadAdminData();
            admins.forEach(adminDto -> {
                try {
                    adminService.createAdmin(adminDto);
                    System.out.println("Successfully seeded admin: " + adminDto.getName());
                } catch (Exception e) {
                    System.err.println("Failed to seed admin " + adminDto.getName() + ": " + e.getMessage());
                }
            });
        } catch (IOException e) {
            System.err.println("Failed to load admin data from JSON: " + e.getMessage());
        }
    }

}
