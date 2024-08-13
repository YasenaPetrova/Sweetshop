package com.academy.cakeshop.dto;

import com.academy.cakeshop.validation.ValidPassword;
import com.academy.cakeshop.validation.ValidPhoneNumber;
import com.academy.cakeshop.validation.ValidRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserRequest(
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
        String role
)
{}