package com.academy.cakeshop.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


public record AccountHistoryResponseDTO(
        Long id,
        LocalDate date,
        String fromAccountId,
        String toAccountId,
        double amount,
        String currency

) {
}
