package com.academy.cakeshop.dto;

public record BankAccountResponse(
        String iban,
        String currency,
        Double balance,
        String ownerName,
        String status
)
{}