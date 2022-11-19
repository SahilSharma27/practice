package com.sahil.Ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Email already registered")  // 404
public class EmailAlreadyRegisteredException extends RuntimeException{
    public EmailAlreadyRegisteredException(String msg){
        super(msg);
    }
}
