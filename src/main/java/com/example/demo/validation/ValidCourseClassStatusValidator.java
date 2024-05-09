package com.example.demo.validation;

import java.util.Arrays;

import com.example.demo.models.entity.enums.CourseClassStatus;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.*;

public class ValidCourseClassStatusValidator implements ConstraintValidator<ValidCourseClassStatus, CourseClassStatus>{
    
    private CourseClassStatus[] values;

    @Override
    public void initialize(ValidCourseClassStatus constraintAnnotation) {
        this.values = constraintAnnotation.anyOf();
    }

    @Override
    public boolean isValid(CourseClassStatus genderEnum, ConstraintValidatorContext context) {
        return Arrays.asList(values).contains(genderEnum);
    }
}
