package com.academy.cakeshop.dto;

import java.time.LocalDate;

public record PurchaseStatDTO(
        LocalDate date,
        Long contractID,
        String contractStatus,
        String productName,
        double quantity,
        String unit,
        double pricePerUnit,
        double gain
) {
}
