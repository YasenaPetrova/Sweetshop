package com.academy.cakeshop.dto;

import com.academy.cakeshop.validation.ValidUnit;
import jakarta.validation.constraints.NotBlank;

public record RecipeDTO(
        @NotBlank(message = "Required field!")
        @ValidUnit
        String name
) {
}