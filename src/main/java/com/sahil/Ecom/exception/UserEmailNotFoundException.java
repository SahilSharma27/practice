package com.sahil.Ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="No Such Email")  // 404
public class UserEmailNotFoundException extends RuntimeException{
    public UserEmailNotFoundException(String message) {
        super(message);
    }
}
