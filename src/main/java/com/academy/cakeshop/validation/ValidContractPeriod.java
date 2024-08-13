package com.academy.cakeshop.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = ContractPeriodValidator.class
)
public @interface ValidContractPeriod {
    String message() default "Invalid contract period!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}