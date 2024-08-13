package com.academy.cakeshop.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;


public record SaleRequestDTO(
        @NotNull(message = "Date is mandatory")
        LocalDate date,


        @NotNull(message = "Amount cannot be null")
        @Positive(message = "Amount must be positive")
        double amount,

        @NotNull(message = "Article ID is mandatory")
        Long articleId
) {
}
