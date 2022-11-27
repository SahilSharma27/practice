package com.sahil.Ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.LOCKED)
public class AccountLockedException extends RuntimeException{

    public AccountLockedException() {
    }

    public AccountLockedException(String message) {
        super(message);
    }
}
