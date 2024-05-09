package com.example.demo.models.DTOs;

public record QualificationDto(
    Long id,
    String name,
    String institution,
    Integer year,
    TeacherDto teacher
) {
}
