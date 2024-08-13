package com.academy.cakeshop.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateValidator implements ConstraintValidator<ValidDate,LocalDate> {
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context){
        return date !=null&&!date.isAfter(LocalDate.now());
    }
}
