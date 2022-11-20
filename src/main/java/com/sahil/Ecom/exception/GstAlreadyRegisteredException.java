package com.sahil.Ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Class message:GST Number already registered")
public class GstAlreadyRegisteredException extends RuntimeException{
    public GstAlreadyRegisteredException(String message) {
        super(message);
    }
}
