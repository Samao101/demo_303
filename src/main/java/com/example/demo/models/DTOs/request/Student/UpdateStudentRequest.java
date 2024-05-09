package com.example.demo.models.DTOs.request.Student;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentRequest extends BaseStudentRequest {
    @NotNull
    private Long id;

    private Boolean isStudy;
}
