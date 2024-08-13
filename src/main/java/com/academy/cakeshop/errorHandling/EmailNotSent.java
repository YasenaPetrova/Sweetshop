package com.academy.cakeshop.errorHandling;

public class EmailNotSent extends RuntimeException {
    public EmailNotSent() {
    }

    public EmailNotSent(String message) {
        super(message);
    }

    public EmailNotSent(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailNotSent(Throwable cause) {
        super(cause);
    }

    public EmailNotSent(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
