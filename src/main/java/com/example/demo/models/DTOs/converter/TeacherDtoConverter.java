package com.example.demo.models.DTOs.converter;

import java.util.Set;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.Repositories.CourseRepository;
import com.example.demo.models.DTOs.TeacherDto;
import com.example.demo.models.entity.Teacher;

@Component
public class TeacherDtoConverter {

    private final CourseRepository courseRepository;

    public TeacherDtoConverter(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public TeacherDto convert(Teacher from) {
        return new TeacherDto(
                from.getId(),
                from.getName(),
                from.getAge(),
                from.getGender(),
                from.getDateOfBirth(),
                from.getPlaceOfBirth(),
                from.getCitizenIdentification(),
                from.getEmail(),
                from.getPhoneNumber(),
                from.getMainCourseId(),
                courseRepository.findByCourseId(from.getMainCourseId()).get().getCourseName(),
                from.getIsAvailable()
        );
    }

    public Set<TeacherDto> convert(Set<Teacher> from) {
        return from.stream().map(this::convert).collect(Collectors.toSet());
    }

    public Optional<TeacherDto> convert(Optional<Teacher> from) {
        return from.map(this::convert);
    }
}
