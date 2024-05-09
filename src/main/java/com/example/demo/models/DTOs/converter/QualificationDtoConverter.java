package com.example.demo.models.DTOs.converter;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.models.DTOs.QualificationDto;
import com.example.demo.models.entity.Qualification;

@Component
public class QualificationDtoConverter {
    private final TeacherDtoConverter teacherDtoConverter;

    public QualificationDtoConverter (TeacherDtoConverter teacherDtoConverter) {
        this.teacherDtoConverter = teacherDtoConverter;
    }
    public QualificationDto convert(Qualification from) {
        return new QualificationDto(
                from.getId(),
                from.getName(),
                from.getInstitution(),
                from.getYear(),
                teacherDtoConverter.convert(from.getTeacher())
        );
    }

    public Set<QualificationDto> convert(Set<Qualification> from) {
        return from.stream().map(this::convert).collect(Collectors.toSet());
    }

    public Optional<QualificationDto> convert(Optional<Qualification> from) {
        return from.map(this::convert);
    }
}
