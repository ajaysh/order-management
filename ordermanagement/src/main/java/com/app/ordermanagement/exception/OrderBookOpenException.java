package com.app.ordermanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class OrderBookOpenException extends RuntimeException {
    public OrderBookOpenException(String message) {
        super(message);
    }
}
