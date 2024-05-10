package com.example.demo.Repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.models.entity.SheetMark;
import com.example.demo.models.entity.enums.CourseLevel;
import com.example.demo.models.entity.enums.SheetMarkStatus;

@Repository
public interface SheetMarkRepository extends JpaRepository<SheetMark, Long>{

    Optional<SheetMark> findByCourseIdAndCourseLevelAndStudentIdAndCourseClassId(String courseId, CourseLevel courseLevel, Long studentId, Long courseClassId);

    @Query("SELECT cc FROM SheetMark cc")
    Set<SheetMark> findAlls();

    Set<SheetMark> findByCourseClassId(Long courseClassId);

    Set<SheetMark> findByStudentId(long studentId);

    Set<SheetMark> findByStudentIdAndSheetMarkStatus(Long studentId, SheetMarkStatus sheetMarkStatus);

    Set<SheetMark> findByStudentIdAndCourseClassId(Long studentId, Long courseClassId);

    Set<SheetMark> findByTeacherId(Long teacherId);

    Set<SheetMark> findByCourseIdAndCourseLevelAndStudentId(String courseId, CourseLevel courseLevel, Long studentId);

    Set<SheetMark> findByCourseIdAndCourseLevelAndStudentIdAndSheetMarkStatus(String courseId, CourseLevel courseLevel, Long studentId, SheetMarkStatus sheetMarkStatus);

    Set<SheetMark> findByStudentIdAndCourseId(Long studentId, String courseId);

    Set<SheetMark> findByStudentIdAndCourseIdAndCourseLevel(Long studentId, String courseId, CourseLevel courseLevel);

    Set<SheetMark> findByStudentIdAndCourseIdAndCourseLevelAndSheetMarkStatus(Long studentId, String courseId, CourseLevel courseLevel, SheetMarkStatus sheetMarkStatus);

    Set<SheetMark> findByCourseId(String courseId);

    Set<SheetMark> findByCourseIdAndStudentIdAndSheetMarkStatus(String courseId, Long studentId,
            SheetMarkStatus sheetMarkStatus);
}
