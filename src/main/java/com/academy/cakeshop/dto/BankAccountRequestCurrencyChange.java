package com.academy.cakeshop.dto;

import com.academy.cakeshop.validation.IBAN;
import com.academy.cakeshop.validation.ValidCurrency;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record BankAccountRequestCurrencyChange(
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
        String fromCurrency,
        @ValidCurrency
        @NotNull(message = "Required field!")
        @Length(min = 1, max = 3, message = "Incorrect field length!")
        String toCurrency
) {
}