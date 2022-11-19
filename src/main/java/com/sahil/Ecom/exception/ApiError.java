package com.sahil.Ecom.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ApiError {

    private LocalDateTime timeStamp;
    private HttpStatus status;
    private String message;
    private List<String> errors;

    public ApiError(LocalDateTime timeStamp,HttpStatus status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(LocalDateTime timeStamp,HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }


}
