package com.academy.cakeshop.dto;

import com.academy.cakeshop.validation.ValidPassword;
import com.academy.cakeshop.validation.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserResponse(
        String firstName,
        String lastName,
        String email,
        String role
)
{}