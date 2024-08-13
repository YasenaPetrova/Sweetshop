package com.academy.cakeshop.dto;

import com.academy.cakeshop.validation.IBAN;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


public record AccountHistoryRequestDTO(

        @NotNull(message = "Date cannot be null")
        LocalDate date,


        @NotBlank(message = "From account cannot be blank")
        @IBAN(message = "Invalis fromAccount IBAN")
        String fromAccount,

        @NotBlank(message = "To account cannot be blank")
        @IBAN(message = "Invalis fromAccount IBAN")
        String toAccount,

        @NotNull(message = "Amount cannot be null")
        @Positive(message = "Amount must be positive")
        Double amount,

        @NotBlank(message = "Currency cannot be blank")
        String currency
) {
}
