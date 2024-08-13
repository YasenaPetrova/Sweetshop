package com.academy.cakeshop.enumeration;

public enum ContractStatus {
    NEW, UNDER_REVISION, APPROVED, DECLINED;

    public static ContractStatus getContractStatusFromString(String contractStatus) {
        return switch (contractStatus.toUpperCase()) {
            case "NEW" -> NEW;
            case "UNDER_REVISION" -> UNDER_REVISION;
            case "APPROVED" -> APPROVED;
            case "DECLINED" -> DECLINED;
            default -> throw new IllegalStateException("Unexpected value: " + contractStatus.toUpperCase());
        };
    }
}