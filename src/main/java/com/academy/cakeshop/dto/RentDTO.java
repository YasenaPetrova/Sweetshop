package com.academy.cakeshop.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record RentDTO(
        @NotNull(message = "Store account ID is mandatory")
        Long storeAccountId,

        @NotNull(message = "Mall account ID is mandatory")
        Long mallAccountId,

        @Positive(message = "Rent amount must be positive")
        double rentAmount,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @NotNull(message = "Due date is mandatory")
        LocalDate dueDate
) {
}
