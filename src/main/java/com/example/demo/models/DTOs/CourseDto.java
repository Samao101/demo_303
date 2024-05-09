package com.example.demo.models.DTOs;

import java.time.DayOfWeek;
import java.util.Set;


public record CourseDto(
    String courseId,
    String courseName,
    Set<DayOfWeek> classDays,
    Integer maxRegistPerRegist,
    Integer maxStuPerClass,
    Boolean isAvailable
) {
}
