package com.academy.cakeshop.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AmountValidator.class)
public @interface ValidAmount {
    String message() default "Invalid amount";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}