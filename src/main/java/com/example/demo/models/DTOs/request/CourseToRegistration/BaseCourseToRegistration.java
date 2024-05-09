package com.example.demo.models.DTOs.request.CourseToRegistration;

import java.time.DayOfWeek;

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
public class BaseCourseToRegistration {
    @NotBlank
    private String courseId;

    @Enumerated(EnumType.STRING)
    private CourseLevel courseLevel;

    @NotNull
    @Positive
    @Min(1)
    @Max(18)
    private Integer startPeriod;

    @NotNull
    @Positive
    @Min(1)
    @Max(18)
    private Integer endPeriod;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Positive
    private Integer maxRegist;

    @Positive
    private Integer maxRegistClass;
}
