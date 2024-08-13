package com.academy.cakeshop.validation;

import com.academy.cakeshop.enumeration.UnitValues;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UnitValidator implements ConstraintValidator<ValidUnit,String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        s = s.toUpperCase();
        return s.equals(UnitValues.KG.toString()) || s.equals(UnitValues.GR.toString()) || s.equals(UnitValues.LT.toString())
                || s.equals(UnitValues.ML.toString()) || s.equals(UnitValues.PCS.toString());
    }
}