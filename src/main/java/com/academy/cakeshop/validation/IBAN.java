package com.academy.cakeshop.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
//@Repeatable(List.class)
//@Documented
@Constraint(
        validatedBy = IBANValidator.class
)

public @interface IBAN {
    String message() default "Invalid IBAN!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
