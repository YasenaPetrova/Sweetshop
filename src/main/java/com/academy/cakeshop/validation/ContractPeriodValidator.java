package com.academy.cakeshop.validation;

import com.academy.cakeshop.enumeration.ContractPeriod;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.engine.config.spi.ConfigurationService;

public class ContractPeriodValidator implements ConstraintValidator<ValidContractPeriod, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.equalsIgnoreCase(ContractPeriod.DAILY.toString())
                || s.equalsIgnoreCase(ContractPeriod.MONTHLY.toString())
                || s.equalsIgnoreCase(ContractPeriod.YEARLY.toString());
    }
}