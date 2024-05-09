package com.example.demo.models.DTOs.converter;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.Repositories.CourseRepository;
import com.example.demo.models.DTOs.CourseToRegistrationDto;
import com.example.demo.models.entity.CourseToRegistration;


@Component
public class CourseToRegistrationDtoConverter {
    
    private final CourseRepository courseRepository;

    public CourseToRegistrationDtoConverter(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public CourseToRegistrationDto convert(CourseToRegistration from){
        return new CourseToRegistrationDto(
                from.getId(),
                from.getCourseId(),
                courseRepository.findByCourseId(from.getCourseId()).get().getCourseName(),
                from.getCourseLevel(),
                from.getStartPeriod(),
                from.getEndPeriod(),
                from.getDayOfWeek(),
                from.getMaxRegist(),
                from.getMaxRegistClass(),
                from.getStudents(),
                from.getStudents().size()
        );
    }

    public Set<CourseToRegistrationDto> convert(Set<CourseToRegistration> from){
        return from.stream().map(this::convert).collect(Collectors.toSet());
    }

    public Optional<CourseToRegistrationDto> convert(Optional<CourseToRegistration> from) {
        return from.map(this::convert);
    }
}
