package com.example.demo.models.DTOs.request.CourseToRegistration;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCourseToRegistration extends BaseCourseToRegistration{
    @NotNull
    private Long id;
}
