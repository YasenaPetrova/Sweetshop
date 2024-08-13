package com.academy.cakeshop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record UserUpdateDTO(
        @NotNull(message = "Required field!")
        @Min(value = 1, message = "No negative values allowed!")
        Long id,
        @NotNull(message = "Required field!")
        @Length(min = 1, max = 255, message = "Incorrect field length!")
        String firstName,
        @NotNull(message = "Required field!")
        @Length(min = 1, max = 255, message = "Incorrect field length!")
        String lastName,
        @Email
        @NotNull(message = "Required field!")
        @Length(min = 1, max = 255, message = "Incorrect field length!")
        String email
) {
}