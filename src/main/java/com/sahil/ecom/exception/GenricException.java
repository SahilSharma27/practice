package com.sahil.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
public class GenricException extends RuntimeException{
    public GenricException() {
    }

    public GenricException(String message) {
        super(message);
    }
}
