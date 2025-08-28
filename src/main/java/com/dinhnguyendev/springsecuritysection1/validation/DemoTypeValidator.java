package com.dinhnguyendev.springsecuritysection1.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DemoTypeValidator implements ConstraintValidator<ValidateDemoType, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
}
