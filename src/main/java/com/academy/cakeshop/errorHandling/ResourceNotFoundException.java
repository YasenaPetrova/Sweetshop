package com.academy.cakeshop.errorHandling;

public class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
}