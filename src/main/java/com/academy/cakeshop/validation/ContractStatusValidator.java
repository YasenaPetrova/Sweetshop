package com.academy.cakeshop.validation;

import com.academy.cakeshop.enumeration.ContractStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ContractStatusValidator implements ConstraintValidator<ValidContractStatus, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.equalsIgnoreCase(ContractStatus.NEW.toString())
                || s.equalsIgnoreCase(ContractStatus.APPROVED.toString())
                || s.equalsIgnoreCase(ContractStatus.DECLINED.toString())
                || s.equalsIgnoreCase(ContractStatus.UNDER_REVISION.toString());
    }
}