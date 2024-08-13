package com.academy.cakeshop.dto;

import com.academy.cakeshop.validation.IBAN;
import com.academy.cakeshop.validation.ValidCurrency;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(
        @NotNull(message = "Required field!")
        @Min(value = 0, message = "No negative values allowed!")
        Long contractID,
        @NotNull(message = "Required field!")
        @IBAN
        String fromIBAN,
        @NotNull(message = "Required field!")
        @IBAN
        String toIBAN,
        @NotNull(message = "Required field!")
        @Min(value = 1, message = "No negative values allowed!")
        Double amount,
        @NotNull(message = "Required field!")
        @ValidCurrency
        String currency
)
{}