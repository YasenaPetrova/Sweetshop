package com.academy.cakeshop.enumeration;

public enum ContractPeriod {
    DAILY, MONTHLY, YEARLY;

    public static ContractPeriod getContractPeriodFromString(String contractPeriod) {
        return switch (contractPeriod.toUpperCase()) {
            case "DAILY" -> DAILY;
            case "MONTHLY" -> MONTHLY;
            case "YEARLY" -> YEARLY;
            default -> throw new IllegalStateException("Unexpected value: " + contractPeriod.toUpperCase());
        };
    }
}