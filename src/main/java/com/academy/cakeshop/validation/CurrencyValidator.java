package com.academy.cakeshop.validation;

import com.academy.cakeshop.enumeration.Currency;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import javax.swing.plaf.ColorUIResource;

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.equalsIgnoreCase(Currency.BGN.toString())
                || s.equalsIgnoreCase(Currency.EUR.toString());
    }
}