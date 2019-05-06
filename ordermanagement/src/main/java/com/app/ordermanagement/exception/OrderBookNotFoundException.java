package com.app.ordermanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderBookNotFoundException extends RuntimeException {
    public OrderBookNotFoundException(String message) {
        super(message);
    }
}