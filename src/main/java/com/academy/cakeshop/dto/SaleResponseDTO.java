package com.academy.cakeshop.dto;



import java.time.LocalDate;

public record SaleResponseDTO(
        LocalDate date,
        double amount,
        Long articleId
) {
}
