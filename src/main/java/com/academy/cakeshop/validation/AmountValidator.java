package com.academy.cakeshop.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AmountValidator implements ConstraintValidator<ValidAmount,Double> {

    @Override
    public boolean isValid(Double amount, ConstraintValidatorContext context){

        return amount !=null && amount>=0;
    }
}
