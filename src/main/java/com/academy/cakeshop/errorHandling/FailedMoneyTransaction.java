package com.academy.cakeshop.errorHandling;

public class FailedMoneyTransaction extends RuntimeException {
    public FailedMoneyTransaction() {
    }

    public FailedMoneyTransaction(String message) {
        super(message);
    }

    public FailedMoneyTransaction(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedMoneyTransaction(Throwable cause) {
        super(cause);
    }

    public FailedMoneyTransaction(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}