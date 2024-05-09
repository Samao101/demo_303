package com.example.demo.models.DTOs.request.Course;

import java.time.DayOfWeek;
import java.util.Set;

import com.example.demo.helper.ValidationMessage;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
public class BaseCourseRequest {
    @NotNull(message = ValidationMessage.Course.COURSE_ID_NOT_NULL)
    @NotEmpty(message = ValidationMessage.Course.COURSE_ID_NOT_EMPTY)
    private String courseId;

    @NotNull(message = ValidationMessage.Course.COURSE_ID_NOT_NULL)
    @NotEmpty(message = ValidationMessage.Course.COURSE_ID_NOT_EMPTY)
    private String courseName;

    @Enumerated(EnumType.STRING)
    private Set<DayOfWeek> classDays;

    @NotNull
    @Positive
    private Integer maxRegistPerRegist;

    @NotNull
    @Positive
    private Integer maxStuPerClass;
}
