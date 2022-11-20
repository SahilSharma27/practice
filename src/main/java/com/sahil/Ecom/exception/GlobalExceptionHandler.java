package com.sahil.Ecom.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<ApiError> handleEmailAlreadyExistsException(EmailAlreadyRegisteredException ex) {
        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(),"email taken");
        return new ResponseEntity<ApiError>(apiError,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PassConfirmPassNotMatchingException.class)
    public ResponseEntity<ApiError> handlePassConfirmPassNotMatchingException(PassConfirmPassNotMatchingException exception){
        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(),"Password Does not match with confirm password");
        return new ResponseEntity<ApiError>(apiError,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CompanyNameAlreadyRegisteredException.class)
    public ResponseEntity<ApiError> handleCompanyNameAlreadyRegisteredException(CompanyNameAlreadyRegisteredException exception){
        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(),"Company Name Already Registered");
        return new ResponseEntity<ApiError>(apiError,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GstAlreadyRegisteredException.class)
    public ResponseEntity<ApiError> handleGstAlreadyRegisteredException(GstAlreadyRegisteredException exception){
        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(),"GST Number Already Registered");
        return new ResponseEntity<ApiError>(apiError,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiError> handleTokenExpiredException(TokenExpiredException exception){
        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(),"TOKEN EXPIRED");
        return new ResponseEntity<ApiError>(apiError,HttpStatus.BAD_REQUEST);
    }

}
