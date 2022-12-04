package com.sahil.Ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UniqueFieldException extends RuntimeException{

    public UniqueFieldException() {
    }

    public UniqueFieldException(String message) {
        super(message);
    }
}
