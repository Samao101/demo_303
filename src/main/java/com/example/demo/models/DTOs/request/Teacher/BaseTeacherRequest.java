package com.example.demo.models.DTOs.request.Teacher;

import java.time.LocalDate;

import com.example.demo.helper.ValidationMessage;
import com.example.demo.models.entity.enums.Gender;
import com.example.demo.validation.ValidGender;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class BaseTeacherRequest {
    @NotNull( message = ValidationMessage.Teacher.TEACHER_NAME_NOT_NULL)
    @NotBlank( message = ValidationMessage.Teacher.TEACHER_NAME_NOT_EMPTY)
    private String name;

    @NotNull( message = ValidationMessage.Teacher.TEACHER_AGE_NOT_NULL)
    @Positive( message = ValidationMessage.Teacher.TEACHER_AGE_NOT_NEGATIVE)
    @Min(16)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @ValidGender(anyOf = {Gender.F, Gender.M})
    private Gender gender;

    @NotNull( message = ValidationMessage.Teacher.TEACHER_Birthday_NULL)
    private LocalDate dateOfBirth;

    @NotNull( message = ValidationMessage.Teacher.TEACHER_PlaceOfBirth_NOT_NULL)
    @NotBlank( message = ValidationMessage.Teacher.TEACHER_PlaceOfBirth_NOT_EMPTY)
    private String placeOfBirth;

    @NotNull( message = ValidationMessage.Teacher.TEACHER_CitizenIdentification_NOT_NULL)
    @NotBlank( message = ValidationMessage.Teacher.TEACHER_CitizenIdentification_NOT_EMPTY)
    private String citizenIdentification;

    @NotNull( message = ValidationMessage.Teacher.TEACHER_Email_NOT_NULL)
    @NotBlank( message = ValidationMessage.Teacher.TEACHER_Email_NOT_EMPTY)
    private String email;

    @NotNull
    @NotBlank
    private String phoneNumber;
}

