package com.example.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidMSSVValidator implements ConstraintValidator<ValidMSSV, String>{

        @Override
        public boolean isValid( String mSSVV, ConstraintValidatorContext context) {
            return mSSVV.length()== 7;
        }
}
