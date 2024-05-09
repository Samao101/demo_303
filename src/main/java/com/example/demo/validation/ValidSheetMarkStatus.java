package com.example.demo.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.demo.models.entity.enums.SheetMarkStatus;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Constraint( validatedBy = ValidSheetMarkValidator.class)
public @interface ValidSheetMarkStatus {
    SheetMarkStatus[] anyOf();
    
    String message() default "Invalid Sheet Mark Status";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

