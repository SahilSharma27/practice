package com.sahil.Ecom.exception;


import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Locale;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    MessageSource messageSource;

    Locale locale = LocaleContextHolder.getLocale();

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<ApiError> handleEmailAlreadyExistsException(EmailAlreadyRegisteredException ex) {

        String message = messageSource.getMessage("already.registered",null,"message",locale);
        String error =  messageSource.getMessage("email.already.registered",null,"message",locale);

        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.BAD_REQUEST,message,error);
        return new ResponseEntity<ApiError>(apiError,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PassConfirmPassNotMatchingException.class)
    public ResponseEntity<ApiError> handlePassConfirmPassNotMatchingException(PassConfirmPassNotMatchingException exception){

        String message = messageSource.getMessage("password.confirm.password",null,"message",locale);
        String error =  messageSource.getMessage("password.confirm.password",null,"message",locale);


        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.BAD_REQUEST,message,error);
        return new ResponseEntity<ApiError>(apiError,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CompanyNameAlreadyRegisteredException.class)
    public ResponseEntity<ApiError> handleCompanyNameAlreadyRegisteredException(CompanyNameAlreadyRegisteredException exception){

        String message = messageSource.getMessage("already.registered",null,"message",locale);
        String error =  messageSource.getMessage("company.name.already.registered",null,"message",locale);

        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.BAD_REQUEST,message,error);
        return new ResponseEntity<ApiError>(apiError,HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(GstAlreadyRegisteredException.class)
    public ResponseEntity<ApiError> handleGstAlreadyRegisteredException(GstAlreadyRegisteredException exception){

        String message = messageSource.getMessage("already.registered",null,"message",locale);
        String error =  messageSource.getMessage("gst.already.registered",null,"message",locale);

        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.BAD_REQUEST,message,error);
        return new ResponseEntity<ApiError>(apiError,HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiError> handleTokenExpiredException(TokenExpiredException exception){
        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(),"TOKEN EXPIRED");
        return new ResponseEntity<ApiError>(apiError,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentialsException(BadCredentialsException exception){
        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.NOT_FOUND,exception.getMessage(),"BAD CRED");
        return new ResponseEntity<ApiError>(apiError,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiError> handleJwtTokenExpiredException(ExpiredJwtException exception){
        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(),"TOKEN EXPIRED");
        return new ResponseEntity<ApiError>(apiError,HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(UsernameNotFoundException.class)
//    public ResponseEntity<ApiError> handleUsernameNotFoundException(UsernameNotFoundException exception){
//        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.NOT_FOUND, exception.getLocalizedMessage(),"USER NAME NOT FOUNDD");
//        return new ResponseEntity<ApiError>(apiError, HttpStatus.NOT_FOUND);
//    }

}
