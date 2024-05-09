package com.example.demo.models.DTOs.request.Teacher;

import java.util.Set;

import com.example.demo.helper.ValidationMessage;
import com.example.demo.models.DTOs.QualificationDto;

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
public class UpdateTeacherRequest extends BaseTeacherRequest{
    @NotNull
    private Long id;

    @NotNull(message = ValidationMessage.Teacher.TEACHER_Main_Course_Id_NOT_NULL)
    @NotBlank(message = ValidationMessage.Teacher.TEACHER_Main_Course_Id_NOT_EMPTY)
    private String mainCourseId;

    @NotNull
    private Boolean isAvailable;

    Set<QualificationDto> qualifications;
}
