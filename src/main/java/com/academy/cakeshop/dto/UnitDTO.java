package com.academy.cakeshop.dto;

import com.academy.cakeshop.validation.ValidUnit;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UnitDTO(
        @NotNull(message = "Base quantity is a required field!")
        @Min(value = 1, message = "Minimal required amount: 1")
        double baseQuantity,
        @NotBlank(message = "Name is a required field!")
        String name
) {
}