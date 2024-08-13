package com.academy.cakeshop.dto;

import java.time.LocalDate;

public record EmployeePaymentDTO(
        String employeeName,
        String role,
        LocalDate date,
        double paymentAmount
) {
}