package com.example.demo.service;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.demo.Repositories.CourseClassRepository;
import com.example.demo.Repositories.CourseRepository;
import com.example.demo.Repositories.SheetMarkRepository;
import com.example.demo.Repositories.StudentRepository;
import com.example.demo.Repositories.TeacherRepository;
import com.example.demo.models.DTOs.request.SheetMark.CreateSheetMark;
import com.example.demo.models.DTOs.request.SheetMark.UpdateSheetMark;
import com.example.demo.models.entity.Course;
import com.example.demo.models.entity.CourseClass;
import com.example.demo.models.entity.SheetMark;
import com.example.demo.models.entity.Student;
import com.example.demo.models.entity.Teacher;
import com.example.demo.models.entity.enums.CourseLevel;
import com.example.demo.models.entity.enums.SheetMarkStatus;

import jakarta.transaction.Transactional;

@Service
public class SheetMarkService {
    private final SheetMarkRepository sheetMarkRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final CourseClassRepository courseClassRepository;

    public SheetMarkService(CourseClassRepository courseClassRepository,
                            TeacherRepository teacherRepository,
                            CourseRepository courseRepository,
                            SheetMarkRepository sheetMarkRepository, 
                            StudentRepository studentRepository) {
        this.sheetMarkRepository = sheetMarkRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.courseClassRepository = courseClassRepository;
    }

    // ALl method for get request
    public Set<SheetMark> getAllSheetMark() {
        return sheetMarkRepository.findAlls();
    }

    public Set<SheetMark> getSheetMarkByCourseClassId(Long courseClassId){
        return sheetMarkRepository.findByCourseClassId(courseClassId);
    }

    public Set<SheetMark> getSheetMarkByStudentId(Long studentId) {
        return sheetMarkRepository.findByStudentId(studentId);
    }

    public Set<SheetMark> getSheetMarkByStudentIdAndStatus(Long studentId, SheetMarkStatus sheetMarkStatus) {
        return sheetMarkRepository.findByStudentIdAndSheetMarkStatus(studentId, sheetMarkStatus);
    }

    public Set<SheetMark> getSheetMarkByStudentIdAndCourseClassId(Long studentId, Long courseClassId) {
        return sheetMarkRepository.findByStudentIdAndCourseClassId(studentId, courseClassId);
    }

    public Set<SheetMark> getSheetMarkByTeacherId(Long teacherId) {
        return sheetMarkRepository.findByTeacherId(teacherId);
    }

    public Set<SheetMark> getSheetMarksByCourseAndStudent(String courseId, CourseLevel courseLevel, Long studentId) {
        return sheetMarkRepository.findByCourseIdAndCourseLevelAndStudentId(courseId, courseLevel, studentId);
    }


    // All method for post requeset
    public SheetMark createSheetMark(CreateSheetMark sheetMarkRequest) {
        Course course= courseRepository.findByCourseId(sheetMarkRequest.getCouresId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id " + sheetMarkRequest.getCouresId()));

        Student student = studentRepository.findById(sheetMarkRequest.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id " + sheetMarkRequest.getStudentId()));

        CourseClass courseClass = courseClassRepository.findById(sheetMarkRequest.getCourseClassId())
                .orElseThrow(() -> new IllegalArgumentException("Course class not found with id " + sheetMarkRequest.getCourseClassId()));

        Teacher teacher = teacherRepository.findById(sheetMarkRequest.getTeacherId())
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id " + sheetMarkRequest.getTeacherId()));   
        
        SheetMark sheetMark = new SheetMark();
        sheetMark.setStudentId(student.getId());
        sheetMark.setCourseId(course.getCourseId());
        sheetMark.setCourseLevel(sheetMarkRequest.getCourseLevel());
        sheetMark.setCourseClassId(courseClass.getId());
        sheetMark.setTeacherId(teacher.getId());

        return sheetMarkRepository.save(sheetMark);
    } 

    // All method for put request
    @Transactional
    public SheetMark updateSheetMark(UpdateSheetMark sheetMarkRequest) {
        SheetMark sheetMark = sheetMarkRepository.findById(sheetMarkRequest.getId())
                .orElseThrow(() -> new RuntimeException("Course not found with id " + sheetMarkRequest.getId()));

        Course course= courseRepository.findByCourseId(sheetMarkRequest.getCouresId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id " + sheetMarkRequest.getCouresId()));

        Student student = studentRepository.findById(sheetMarkRequest.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id " + sheetMarkRequest.getStudentId()));

        CourseClass courseClass = courseClassRepository.findById(sheetMarkRequest.getCourseClassId())
                .orElseThrow(() -> new IllegalArgumentException("Course class not found with id " + sheetMarkRequest.getCourseClassId()));

        Teacher teacher = teacherRepository.findById(sheetMarkRequest.getTeacherId())
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id " + sheetMarkRequest.getTeacherId()));


        if(!course.getCourseId().equals(sheetMark.getCourseId())) {
            throw new IllegalArgumentException("Course id of this sheet mark can not be change!!!");
        }

        if(!student.getId().equals(sheetMark.getStudentId())) {
            throw new IllegalArgumentException("Student id of this sheet mark can not be change!!!");
        }

        if(!courseClass.getId().equals(sheetMark.getCourseClassId())) {
            throw new IllegalArgumentException("Course class id of this sheet mark can not be change!!!");
        }

        if(!teacher.getId().equals(sheetMark.getTeacherId())) {
            throw new IllegalArgumentException("Teacher id of this sheet mark can not be change!!!");
        }

        sheetMark.setAssignmentScore(sheetMarkRequest.getAssignmentScore());
        sheetMark.setProjectScore(sheetMarkRequest.getProjectScore());
        sheetMark.setMidTermScore(sheetMarkRequest.getMidTermScore());
        sheetMark.setFinalExamScore(sheetMarkRequest.getFinalExamScore());

        sheetMark.setSheetMarkStatus(sheetMarkRequest.getSheetMarkStatus());
        return sheetMarkRepository.save(sheetMark);
    }


    public void assignScores(Long studentId, Long courseClassId, Double assignmentScore, Double projectScore, Double midTermScore, Double finalExamScore) {
        Set<SheetMark> sheetMarks = sheetMarkRepository.findByStudentIdAndCourseClassId(studentId, courseClassId);

        if (sheetMarks.isEmpty()) {
            throw new IllegalArgumentException("No SheetMark found for the given studentId and courseClassId.");
        }

        for (SheetMark sheetMark : sheetMarks) {
            if (assignmentScore != null) {
                sheetMark.setAssignmentScore(assignmentScore);
            }
            if (projectScore != null) {
                sheetMark.setProjectScore(projectScore);
            }
            if (midTermScore != null) {
                sheetMark.setMidTermScore(midTermScore);
            }
            if (finalExamScore != null) {
                sheetMark.setFinalExamScore(finalExamScore);
            }
            sheetMark.updateFinalGrade(); // Assuming this method exists to recalculate final grade
            sheetMarkRepository.save(sheetMark);
        }
    }

    
    // All method for delete request
    public void deleteCourse(Long sheetMarkId) {
        SheetMark sheetMark = sheetMarkRepository.findById(sheetMarkId)
                .orElseThrow(() -> new RuntimeException("Sheet mark not found with id " + sheetMarkId));
        sheetMarkRepository.delete(sheetMark);
    }

    public void deleteAllSheetMark() {
        sheetMarkRepository.deleteAll();
    }
}
