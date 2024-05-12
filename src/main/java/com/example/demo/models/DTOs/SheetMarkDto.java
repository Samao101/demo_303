package com.example.demo.models.DTOs;

import com.example.demo.models.entity.enums.CourseLevel;
import com.example.demo.models.entity.enums.SheetMarkStatus;

public record SheetMarkDto(
    Long studentId,
    String studentName,
    String courseId,
    String courseName,
    CourseLevel courseLevel,
    Long courseClassId,
    Long teacherId,
    Double assignmentScore,
    Double projectScore,
    Double midTermScore,
    Double finalExamScore,
    SheetMarkStatus sheetMarkStatus,
    Double finalGrade
) {
}
