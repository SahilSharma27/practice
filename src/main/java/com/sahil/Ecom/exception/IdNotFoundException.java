package com.sahil.Ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class IdNotFoundException extends RuntimeException{

    public IdNotFoundException() {
    }

    public IdNotFoundException(String message) {
        super(message);
    }
}
