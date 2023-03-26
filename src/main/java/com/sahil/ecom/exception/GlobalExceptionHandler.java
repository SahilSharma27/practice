package com.sahil.ecom.exception;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;

@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<ApiError> handleGenericException(GenericException ex) {

        String message =  ex.getMessage();
        String error = ex.getLocalizedMessage();
//        String message = messageSource.getMessage(ex.getMessage(), null, "message", LocaleContextHolder.getLocale());
//        String error = messageSource.getMessage(ex.getMessage(), null, "message", LocaleContextHolder.getLocale());

        ApiError apiError = new ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST, message, error);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiError> handleTokenExpiredException(TokenExpiredException exception) {

        String message = messageSource.getMessage("time.limit.message", null, "message", LocaleContextHolder.getLocale());
        String error = messageSource.getMessage("token.expired", null, "message", LocaleContextHolder.getLocale());


        ApiError apiError = new ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST, message, error);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentialsException(BadCredentialsException exception) {

//        String message = messageSource.getMessage("time.limit.message",null,"message",locale);
        String error = messageSource.getMessage("user.login.bad.credentials", null, "message", LocaleContextHolder.getLocale());

        ApiError apiError = new ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST, exception.getMessage(), error);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiError> handleJwtTokenExpiredException(ExpiredJwtException exception) {
        ApiError apiError = new ApiError(LocalDateTime.now(), HttpStatus.BAD_REQUEST, exception.getLocalizedMessage(), "TOKEN EXPIRED");
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiError> handleInvalidTokenException(InvalidTokenException exception) {

        String message = messageSource.getMessage("token.not.valid", null, "message", LocaleContextHolder.getLocale());
        String error = messageSource.getMessage("token.not.valid", null, "message", LocaleContextHolder.getLocale());
        ApiError apiError = new ApiError(LocalDateTime.now(), HttpStatus.FORBIDDEN, message, error);
        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);

    }


    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ApiError> handleMalformedJwtException(MalformedJwtException exception) {

        String message = messageSource.getMessage("token.not.valid", null, "message", LocaleContextHolder.getLocale());
        String error = messageSource.getMessage("token.not.valid", null, "message", LocaleContextHolder.getLocale());

        ApiError apiError = new ApiError(LocalDateTime.now(), HttpStatus.UNAUTHORIZED, message, error);
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);

    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ApiError> handleFileNotFoundException(FileNotFoundException exception) {

        String message = messageSource.getMessage("id.not.found", null, "message", LocaleContextHolder.getLocale());
        String error = messageSource.getMessage("id.not.found", null, "message", LocaleContextHolder.getLocale());

        ApiError apiError = new ApiError(LocalDateTime.now(), HttpStatus.NOT_FOUND, message, error);
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);

    }


}
