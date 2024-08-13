package com.academy.cakeshop.validation;

import com.academy.cakeshop.enumeration.BankAccountStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AccountStatusValidator implements ConstraintValidator<ValidAccountStatus, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.equalsIgnoreCase(BankAccountStatus.ACTIVE.toString())
                || s.equalsIgnoreCase(BankAccountStatus.INACTIVE.toString());
    }
}