package com.example.demo.validation;

import java.util.Arrays;

import com.example.demo.models.entity.enums.SheetMarkStatus;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.*;

public class ValidSheetMarkValidator implements ConstraintValidator<ValidSheetMarkStatus, SheetMarkStatus>{
    
    private SheetMarkStatus[] values;

    @Override
    public void initialize(ValidSheetMarkStatus constraintAnnotation) {
        this.values = constraintAnnotation.anyOf();
    }

    @Override
    public boolean isValid(SheetMarkStatus sheetMarkStatus, ConstraintValidatorContext context) {
        return Arrays.asList(values).contains(sheetMarkStatus);
    }
}
