package com.academy.cakeshop.validation;

import com.academy.cakeshop.enumeration.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoleValidator implements ConstraintValidator<ValidRole, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s.equalsIgnoreCase(Role.STORE.toString())
                || s.equalsIgnoreCase(Role.MALL.toString())
                || s.equalsIgnoreCase(Role.MANAGER.toString())
                || s.equalsIgnoreCase(Role.SUPPLIER.toString())
                || s.equalsIgnoreCase(Role.EMPLOYEE.toString())
                || s.equalsIgnoreCase(Role.CLIENT.toString())
                || s.equalsIgnoreCase(Role.ADMIN.toString());
    }
}