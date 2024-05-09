package com.example.demo.models.DTOs;

import java.time.DayOfWeek;
import java.util.Set;

import com.example.demo.models.entity.enums.CourseLevel;

public record CourseToRegistrationDto(
    Long id,
    String courseId,
    String courseName,
    CourseLevel courseLevel,
    Integer startPeriod,
    Integer endPeriod,
    DayOfWeek dayOfWeek,
    Integer maxRegist,
    Integer maxRegistClass, 
    Set<Long> students,
    Integer numberOfStudent
) {

}
