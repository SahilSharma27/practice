package com.sahil.Ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Class Message :Company Name already registered")
public class CompanyNameAlreadyRegisteredException extends RuntimeException{
    public CompanyNameAlreadyRegisteredException(String message) {
        super(message);
    }
}
