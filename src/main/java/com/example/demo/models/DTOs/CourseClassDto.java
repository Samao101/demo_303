package com.example.demo.models.DTOs;

import java.time.DayOfWeek;
import java.util.Set;

import com.example.demo.models.entity.enums.CourseClassStatus;
import com.example.demo.models.entity.enums.CourseLevel;

public record CourseClassDto(
    Long id,
    String courseId,
    String courseName,
    CourseLevel courseLevel,
    Integer startPeriod,
    Integer endPeriod,
    DayOfWeek dayOfWeek,
    Long teacherId,
    Integer capacity,
    Integer studentCount,
    Set<Long> studentIds,
    CourseClassStatus classStatus,
    Integer numberOfStudent
) {
}
