package com.academy.cakeshop.errorHandling;

public class QuantityTooLow extends RuntimeException{
    public QuantityTooLow() {
    }

    public QuantityTooLow(String message) {
        super(message);
    }

    public QuantityTooLow(String message, Throwable cause) {
        super(message, cause);
    }

    public QuantityTooLow(Throwable cause) {
        super(cause);
    }

    public QuantityTooLow(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
