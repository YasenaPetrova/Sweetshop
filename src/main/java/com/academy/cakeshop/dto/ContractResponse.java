package com.academy.cakeshop.dto;

public record ContractResponse(
        Double contractSum,
        String currency,
        String contractPeriod,
        String contractorName
)
{}