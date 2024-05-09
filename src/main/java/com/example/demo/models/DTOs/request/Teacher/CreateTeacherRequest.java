package com.example.demo.models.DTOs.request.Teacher;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class CreateTeacherRequest extends BaseTeacherRequest{
    @NotNull
    @NotBlank
    private String mainCourseId;
}
