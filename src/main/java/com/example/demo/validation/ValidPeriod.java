package com.example.demo.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Constraint(validatedBy = ValidPeriodValidator.class)
public @interface ValidPeriod {

    String message() default "Invalid Period";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
