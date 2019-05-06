package com.app.ordermanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateOrderBookException extends RuntimeException {
    public DuplicateOrderBookException(String message) {
        super(message);
    }
}