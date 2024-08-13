package com.academy.cakeshop.enumeration;

public enum Currency {
    BGN, EUR;

    public static Currency getCurrencyFromString(String currency) {
        return switch (currency.toUpperCase()) {
            case "BGN" -> BGN;
            case "EUR" -> EUR;
            default -> throw new IllegalStateException("Unexpected value: " + currency.toUpperCase());
        };
    }
}