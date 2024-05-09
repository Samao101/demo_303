package com.example.demo.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint( validatedBy = ValidMSSVValidator.class)
public @interface ValidMSSV {
    String message() default "Invalid MSSV";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
