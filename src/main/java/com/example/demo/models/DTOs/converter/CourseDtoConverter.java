package com.example.demo.models.DTOs.converter;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.models.DTOs.CourseDto;
import com.example.demo.models.entity.Course;

@Component
public class CourseDtoConverter {
    public CourseDto convert(Course from) {
        return new CourseDto(
            from.getCourseId(),
            from.getCourseName(),
            from.getClassDays(),
            from.getMaxRegistPerRegist(),
            from.getMaxStuPerClass(),
            from.getIsAvailable()
        );
    }

    public Set<CourseDto> convert(Set<Course> from) {
        return from.stream().map(this::convert).collect(Collectors.toSet());
    }

    public Optional<CourseDto> convert(Optional<Course> from) {
        return from.map(this::convert);
    }
}
