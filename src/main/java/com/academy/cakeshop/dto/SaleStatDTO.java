package com.academy.cakeshop.dto;

public record SaleStatDTO(
        String articleName,
        double articlePrice,
        int quantity
) {
}
