package com.example.demo.models.DTOs.converter;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.models.DTOs.StudentDto;
import com.example.demo.models.entity.Student;

@Component
public class StudentDtoConverter {

    public StudentDto convert(Student from) {
        return new StudentDto(
                from.getId(),
                from.getName(),
                from.getAge(),
                from.getGender(),
                from.getDateOfBirth(),
                from.getPlaceOfBirth(),
                from.getCitizenIdentification(),
                from.getEmail(),
                from.getPhoneNumber(),
                from.getIsStudy()
        );
    }

    public Set<StudentDto> convert(Set<Student> from) {
        return from.stream().map(this::convert).collect(Collectors.toSet());
    }

    public Optional<StudentDto> convert(Optional<Student> from) {
        return from.map(this::convert);
    }
}
