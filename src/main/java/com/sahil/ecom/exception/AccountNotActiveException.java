package com.sahil.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class AccountNotActiveException extends RuntimeException{

    public AccountNotActiveException() {
    }

    public AccountNotActiveException(String message) {
        super(message);
    }
}
