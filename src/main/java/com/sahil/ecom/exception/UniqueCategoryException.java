package com.sahil.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UniqueCategoryException extends RuntimeException{

    public UniqueCategoryException() {
    }

    public UniqueCategoryException(String message) {
        super(message);
    }
}
