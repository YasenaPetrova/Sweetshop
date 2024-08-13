package com.academy.cakeshop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


public record PurchaseOrderRequestDTO(
        @NotNull(message = "Quantity is mandatory")
        @Positive(message = "Quantity must be a positive value")
        Integer quantity,

        @NotNull(message = "Price is mandatory")
        @Positive(message = "Price must be a positive value")
        Double price,
        @NotBlank(message = "Date is mandatory")
        LocalDate date,

        @NotBlank(message = "Status is mandatory")
        String status,

        @NotNull(message = "Product ID is mandatory")
        Long productId,

        @NotNull(message = "Unit ID is mandatory")
        Long unitId,

        @NotNull(message = "Status is mandatory")
        String bankAccountStatus,

        @NotNull(message = "Contract ID is mandatory")
        Long contractId

) {
}
