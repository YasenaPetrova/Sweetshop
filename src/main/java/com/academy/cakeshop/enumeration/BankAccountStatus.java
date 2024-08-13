package com.academy.cakeshop.enumeration;

import com.academy.cakeshop.persistance.entity.BankAccount;
import com.academy.cakeshop.service.BankAccountService;

public enum BankAccountStatus {
    ACTIVE, INACTIVE;
    public static BankAccountStatus getBankAccountStatusFromString(String bankAccountStatus) {
        return switch (bankAccountStatus.toUpperCase()) {
            case "ACTIVE" -> ACTIVE;
            case "INACTIVE" -> INACTIVE;
            default -> throw new IllegalStateException("Unexpected value: " + bankAccountStatus.toUpperCase());
        };
    }
}