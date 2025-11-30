package com.springbootdemo.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BirthdayValidator implements ConstraintValidator<BirthdayAnnotation, LocalDate> {

    private int min;

    @Override
    public void initialize(BirthdayAnnotation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        long years = ChronoUnit.YEARS.between(localDate, LocalDate.now());
        return years>=min;
    }
}
