package com.sahil.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)  // 404
public class EmailAlreadyRegisteredException extends RuntimeException{

    public EmailAlreadyRegisteredException() {
    }

    public EmailAlreadyRegisteredException(String msg){
        super(msg);
    }
}
