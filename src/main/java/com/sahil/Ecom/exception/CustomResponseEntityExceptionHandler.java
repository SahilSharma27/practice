package com.sahil.Ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;

import java.time.LocalDateTime;

//@ControllerAdvice
//public class CustomResponseEntityExceptionHandler extends ResponseStatusExceptionHandler {
//
//    @ExceptionHandler(UsernameNotFoundException.class)
//    public final ResponseEntity<ApiError> handleUserNotFoundException(Exception e, WebRequest webRequest){
//
//        ApiError apiError= new ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST, e.getMessage(),webRequest.getDescription(false));
//
//        return new ResponseEntity<ApiError>(apiError,HttpStatus.);
//
//    }
//
//}
