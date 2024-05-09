package com.example.demo.models.DTOs.request.CourseClass;

import java.time.DayOfWeek;

import com.example.demo.models.entity.enums.CourseClassStatus;
import com.example.demo.models.entity.enums.CourseLevel;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseCourseClass {
    @NotBlank
    private String courseId;

    @Enumerated(EnumType.STRING)
    private CourseLevel courseLevel;

    @NotNull
    @Positive
    @Min(1)
    @Max(18)
    Integer startPeriod;

    @NotNull
    @Positive
    @Min(1)
    @Max(18)
    Integer endPeriod;

    @Enumerated(EnumType.STRING)
    DayOfWeek dayOfWeek;

    @NotNull
    private Long teacherId;

    @NotNull
    @Positive
    private Integer capacity;

    @NotNull
    private CourseClassStatus courseClassStatus;
    
}
