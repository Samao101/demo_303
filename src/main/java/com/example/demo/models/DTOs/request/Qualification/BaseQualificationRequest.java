package com.example.demo.models.DTOs.request.Qualification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseQualificationRequest {

    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String institution;

    @Positive
    private Integer year;

    @NotNull
    private Long teacherId;
}
