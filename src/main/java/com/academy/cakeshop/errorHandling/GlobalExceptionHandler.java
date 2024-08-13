package com.academy.cakeshop.errorHandling;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<String> BusinessNotFoundHandler(BusinessNotFound businessNotFound) {
        return new ResponseEntity<String>(businessNotFound.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> FailedMoneyTransactionHandler(FailedMoneyTransaction failedMoneyTransaction) {
        return new ResponseEntity<String>(failedMoneyTransaction.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }
    @ExceptionHandler
    public ResponseEntity<String> QuantityTooLowHandler(QuantityTooLow quantityTooLow) {
        return new ResponseEntity<String>(quantityTooLow.getMessage(), HttpStatus.INSUFFICIENT_STORAGE);
    }

    @ExceptionHandler
    public ResponseEntity<String> IllegalStateException(IllegalStateException illegalStateException) {
        return new ResponseEntity<String>(illegalStateException.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        LinkedHashMap<String, String> validationErrors = new LinkedHashMap<>();
        for (ObjectError error: errors) {
            validationErrors.put(((FieldError)error).getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }
}