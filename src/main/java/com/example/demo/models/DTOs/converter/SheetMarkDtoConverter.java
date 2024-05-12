package com.example.demo.models.DTOs.converter;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.demo.Repositories.CourseRepository;
import com.example.demo.Repositories.StudentRepository;
import com.example.demo.models.DTOs.SheetMarkDto;
import com.example.demo.models.entity.SheetMark;

@Component
public class SheetMarkDtoConverter {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public SheetMarkDtoConverter(CourseRepository courseRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    public SheetMarkDto convert(SheetMark from) {
        return new SheetMarkDto(
                from.getStudentId(),
                studentRepository.findById(from.getStudentId()).get().getName(),
                from.getCourseId(),
                courseRepository.findByCourseId(from.getCourseId()).get().getCourseName(),
                from.getCourseLevel(),
                from.getCourseClassId(),
                from.getTeacherId(),
                from.getAssignmentScore(),
                from.getProjectScore(),
                from.getMidTermScore(),
                from.getFinalExamScore(),
                from.getSheetMarkStatus(),
                from.getFinalGrade()
        );
    }

    public Set<SheetMarkDto> convert(Set<SheetMark> from) {
        return from.stream().map(this::convert).collect(Collectors.toSet());
    }

    public Optional<SheetMarkDto> convert(Optional<SheetMark> from) {
        return from.map(this::convert);
    }
}
