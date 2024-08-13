package com.academy.cakeshop.dto;

import com.academy.cakeshop.validation.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record NewUserAccountDTO(
        @NotNull(message = "Required field!")
        @Length(min = 1, max = 255, message = "Incorrect field length!")
        String firstName,
        @NotNull(message = "Required field!")
        @Length(min = 1, max = 255, message = "Incorrect field length!")
        String lastName,
        @NotNull(message = "Required field!")
        @Length(min = 1, max = 255, message = "Incorrect field length!")
        String userName,
        @ValidPassword
        @NotNull(message = "Required field!")
        @Length(min = 1, max = 255, message = "Incorrect field length!")
        String password,
        @Email
        @NotNull(message = "Required field!")
        @Length(min = 1, max = 255, message = "Incorrect field length!")
        String email,
        @ValidPhoneNumber
        @NotNull(message = "Required field!")
        @Length(min = 1, max = 255, message = "Incorrect field length!")
        String phoneNumber,
        @NotNull(message = "Required field!")
        @Length(min = 1, max = 255, message = "Incorrect field length!")
        String address,
        @ValidRole
        @NotNull(message = "Required field!")
        @Length(min = 1, max = 255, message = "Incorrect field length!")
        String role,
        @IBAN
        @NotNull(message = "Required field!")
        @Length(min = 1, max = 255, message = "Incorrect field length!")
        String iban,
        @NotNull(message = "Required field!")
        @Min(value = 1, message = "Minimal amount 1.0")
        Double balance,
        @ValidCurrency
        @NotNull(message = "Required field!")
        @Length(min = 1, max = 3, message = "Incorrect field length!")
        String currency,
        @Min(value = 0)
        double contractSum
) {
}