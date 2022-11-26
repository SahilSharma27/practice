package com.sahil.Ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)  // 404
public class PassConfirmPassNotMatchingException extends RuntimeException{

    public PassConfirmPassNotMatchingException() {
    }

    //
    public PassConfirmPassNotMatchingException(String message) {
        super(message);
    }
}
