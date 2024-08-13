package com.academy.cakeshop.dto;

public record PaymentResponse(
        String fromIBAN,
        String toIBAN,
        Double amount,
        String currency
)
{}