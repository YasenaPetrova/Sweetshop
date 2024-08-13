package com.academy.cakeshop.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.iban4j.IbanFormatException;
import org.iban4j.IbanUtil;

public class IBANValidator implements ConstraintValidator<IBAN, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            IbanUtil.validate(s);
            return true;
        } catch (IbanFormatException e) {
            return false;
        }
    }
}