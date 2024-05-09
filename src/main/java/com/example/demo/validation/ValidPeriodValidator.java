package com.example.demo.validation;

import com.example.demo.models.entity.CourseClass;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidPeriodValidator implements ConstraintValidator<ValidPeriod, CourseClass>{
    @Override
    public void initialize(ValidPeriod constraintAnnotation) {
    }

    @Override
    public boolean isValid(CourseClass courseClass, ConstraintValidatorContext context) {
        if (courseClass.getStartPeriod() == null || courseClass.getEndPeriod() == null) {
            return true; // Null được coi là valid, hoặc bạn có thể return false tùy ý
        }
        return courseClass.getEndPeriod() > courseClass.getStartPeriod();
    }
}
