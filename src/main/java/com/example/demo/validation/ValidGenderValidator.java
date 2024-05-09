package com.example.demo.validation;

import java.util.Arrays;

import com.example.demo.models.entity.enums.Gender;

import jakarta.validation.*;

public class ValidGenderValidator implements ConstraintValidator<ValidGender, Gender> {

    private Gender[] values;

    @Override
    public void initialize(ValidGender constraintAnnotation) {
        this.values = constraintAnnotation.anyOf();
    }

    @Override
    public boolean isValid(Gender genderEnum, ConstraintValidatorContext context) {
        return Arrays.asList(values).contains(genderEnum);
    }
}