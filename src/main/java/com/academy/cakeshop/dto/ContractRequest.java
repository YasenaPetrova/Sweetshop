package com.academy.cakeshop.dto;

import com.academy.cakeshop.validation.ValidContractPeriod;
import com.academy.cakeshop.validation.ValidContractStatus;
import com.academy.cakeshop.validation.ValidCurrency;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ContractRequest(
        @NotNull(message = "Required field!")
        @Min(value = 1, message = "Minimal contract sum = 1!")
        Double contractSum,
        @ValidCurrency
        @NotNull(message = "Required field!")
        String currency,
        @ValidContractPeriod
        @NotNull(message = "Required field!")
        String contractPeriod,
        @ValidContractStatus
        @NotNull(message = "Required field!")
        String contractStatus
)
{}