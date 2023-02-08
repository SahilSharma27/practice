package com.sahil.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)  // 404
public class UserEmailNotFoundException extends RuntimeException{

    public UserEmailNotFoundException() {
    }

    public UserEmailNotFoundException(String message) {
        super(message);
    }
}
