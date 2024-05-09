package com.example.demo.models.DTOs;

import java.time.LocalDate;

import com.example.demo.models.entity.enums.Gender;

public record StudentDto(
    Long id,
    String name,
    Integer age,
    Gender gender,
    LocalDate dateOfBirth,
    String placeOfBirth,
    String citizenIdentification,
    String email,
    String phoneNumber,
    Boolean isStudy
) {
}
