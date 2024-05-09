package com.example.demo.models.DTOs.request.Admin;

import java.time.LocalDate;

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
public class BaseAdminRequest {
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @Positive
    @Min(16)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @ValidGender(anyOf = {Gender.F, Gender.M})
    private Gender gender;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    @NotBlank
    private String placeOfBirth;

    @NotNull
    @NotBlank
    private String citizenIdentification;

    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    private String phoneNumber;
}
