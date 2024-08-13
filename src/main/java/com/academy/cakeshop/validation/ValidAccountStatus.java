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
        validatedBy = AccountStatusValidator.class
)
public @interface ValidAccountStatus {
    String message() default "Invalid input!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}