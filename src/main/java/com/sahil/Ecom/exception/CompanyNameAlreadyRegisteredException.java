package com.sahil.Ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class CompanyNameAlreadyRegisteredException extends RuntimeException{

    public CompanyNameAlreadyRegisteredException() {
    }

    public CompanyNameAlreadyRegisteredException(String message) {
        super(message);
    }
}
