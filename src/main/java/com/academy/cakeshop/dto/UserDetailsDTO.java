package com.academy.cakeshop.dto;

public record UserDetailsDTO(
        String fullName,
        String userName,
        String email,
        String role,
        String iban,
        String currency,
        Double balance,
        String status
) {
}