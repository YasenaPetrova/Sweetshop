package com.academy.cakeshop.enumeration;

public enum Role {
    STORE, MALL, SUPPLIER, MANAGER, EMPLOYEE, CLIENT, ADMIN;

    public static Role getRoleFromString(String role) {
        return switch (role.toUpperCase()) {
            case "STORE" -> STORE;
            case "MALL" -> MALL;
            case "SUPPLIER" -> SUPPLIER;
            case "MANAGER" -> MANAGER;
            case "EMPLOYEE" -> EMPLOYEE;
            case "CLIENT" -> CLIENT;
            case "ADMIN" -> ADMIN;
            default -> throw new IllegalStateException("Unexpected value: " + role.toUpperCase());
        };
    }
}