package com.academy.cakeshop.dto;

import com.academy.cakeshop.validation.ValidUnit;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ProductDTO(
        @NotBlank(message = "Required field!")
        @Min(value = 1,message = "Minimal required amount: 1")
        double pricePerUnit,
        @NotBlank(message = "Required field!")
        @ValidUnit
        String name
) {
}