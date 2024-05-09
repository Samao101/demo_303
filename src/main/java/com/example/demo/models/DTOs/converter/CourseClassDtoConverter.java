package com.example.demo.models.DTOs.converter;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.Repositories.CourseRepository;
import com.example.demo.models.DTOs.CourseClassDto;
import com.example.demo.models.entity.CourseClass;

@Component
public class CourseClassDtoConverter {
    private final CourseRepository courseRepository;

    public CourseClassDtoConverter(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public CourseClassDto convert(CourseClass from){
        return new CourseClassDto(
                from.getId(),
                from.getCourseId(),
                courseRepository.findByCourseId(from.getCourseId()).get().getCourseName(),
                from.getCourseLevel(),
                from.getStartPeriod(),
                from.getEndPeriod(),
                from.getDayOfWeek(),
                from.getTeacherId(),
                from.getCapacity(),
                from.getStudentCount(),
                from.getStudentIds(),
                from.getCourseClassStatus(),
                from.getStudentCount()
        );
    }

    public Set<CourseClassDto> convert(Set<CourseClass> from){
        return from.stream().map(this::convert).collect(Collectors.toSet());
    }

    public Optional<CourseClassDto> convert(Optional<CourseClass> from) {
        return from.map(this::convert);
    }
}
