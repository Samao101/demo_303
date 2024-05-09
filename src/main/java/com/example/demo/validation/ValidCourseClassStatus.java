package com.example.demo.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.demo.models.entity.enums.CourseClassStatus;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Constraint( validatedBy = ValidCourseClassStatusValidator.class)
public @interface ValidCourseClassStatus {
    CourseClassStatus[] anyOf();
    
    String message() default "Invalid CourseClass Status";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
