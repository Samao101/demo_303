package com.example.demo.models.DTOs.request.SheetMark;

import com.example.demo.models.entity.enums.CourseLevel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseSheetMarkRequest {
    @NotNull
    private Long studentId;
    
    @NotNull
    @NotBlank
    private String couresId;

    @NotNull
    private CourseLevel courseLevel;

    @NotNull
    private Long courseClassId;

    @NotNull
    private Long teacherId;
}
