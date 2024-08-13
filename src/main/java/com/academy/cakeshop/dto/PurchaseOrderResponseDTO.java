package com.academy.cakeshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


public record PurchaseOrderResponseDTO(
        Long id,
        Integer quantity,
        Double price,
        LocalDate date,
        String status,
        Long productId,
        Long unitId,
        Long contractId
) {
}
