package com.sahil.Ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class GstAlreadyRegisteredException extends RuntimeException{

    public GstAlreadyRegisteredException() {
    }

    public GstAlreadyRegisteredException(String message) {
        super(message);
    }


}
