package com.sahil.ecom.exception;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<ApiError> handleEmailAlreadyExistsException(EmailAlreadyRegisteredException ex) {

        String message = messageSource.getMessage("already.registered",null,"message",LocaleContextHolder.getLocale());
        String error =  messageSource.getMessage("email.already.registered",null,"message",LocaleContextHolder.getLocale());

        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.BAD_REQUEST,message,error);
        return new ResponseEntity<ApiError>(apiError,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PassConfirmPassNotMatchingException.class)
    public ResponseEntity<ApiError> handlePassConfirmPassNotMatchingException(PassConfirmPassNotMatchingException exception){

        String message = messageSource.getMessage("password.confirm.password",null,"message",LocaleContextHolder.getLocale());
        String error =  messageSource.getMessage("password.confirm.password",null,"message",LocaleContextHolder.getLocale());


        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.BAD_REQUEST,message,error);
        return new ResponseEntity<ApiError>(apiError,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CompanyNameAlreadyRegisteredException.class)
    public ResponseEntity<ApiError> handleCompanyNameAlreadyRegisteredException(CompanyNameAlreadyRegisteredException exception){

        String message = messageSource.getMessage("already.registered",null,"message",LocaleContextHolder.getLocale());
        String error =  messageSource.getMessage("company.name.already.registered",null,"message",LocaleContextHolder.getLocale());

        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.BAD_REQUEST,message,error);
        return new ResponseEntity<ApiError>(apiError,HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(GstAlreadyRegisteredException.class)
    public ResponseEntity<ApiError> handleGstAlreadyRegisteredException(GstAlreadyRegisteredException exception){

        String message = messageSource.getMessage("already.registered",null,"message",LocaleContextHolder.getLocale());
        String error =  messageSource.getMessage("gst.already.registered",null,"message",LocaleContextHolder.getLocale());

        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.BAD_REQUEST,message,error);
        return new ResponseEntity<ApiError>(apiError,HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiError> handleTokenExpiredException(TokenExpiredException exception){

        String message = messageSource.getMessage("time.limit.message",null,"message",LocaleContextHolder.getLocale());
        String error =  messageSource.getMessage("token.expired",null,"message",LocaleContextHolder.getLocale());


        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.BAD_REQUEST,message,error);
        return new ResponseEntity<ApiError>(apiError,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentialsException(BadCredentialsException exception){

//        String message = messageSource.getMessage("time.limit.message",null,"message",locale);
        String error =  messageSource.getMessage("user.login.bad.credentials",null,"message",LocaleContextHolder.getLocale());

        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.BAD_REQUEST,exception.getMessage(),error);
        return new ResponseEntity<ApiError>(apiError,HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler(AccountNotActiveException.class)
    public ResponseEntity<ApiError> handleAccountNotActiveException(AccountNotActiveException exception){

        String message = messageSource.getMessage("account.not.active",null,"message",LocaleContextHolder.getLocale());
        String error =messageSource.getMessage("account.not.active",null,"message",LocaleContextHolder.getLocale());
        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.FORBIDDEN,message,error);
        return new ResponseEntity<ApiError>(apiError, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<ApiError> handleAccountLockedException(AccountLockedException exception){

        String message = messageSource.getMessage("account.locked",null,"message",LocaleContextHolder.getLocale());
        String error =messageSource.getMessage("account.locked",null,"message",LocaleContextHolder.getLocale());
        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.FORBIDDEN,message,error);
        return new ResponseEntity<ApiError>(apiError, HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiError> handleInvalidTokenException(InvalidTokenException exception){

        String message = messageSource.getMessage("token.not.valid",null,"message",LocaleContextHolder.getLocale());
        String error =messageSource.getMessage("token.not.valid",null,"message",LocaleContextHolder.getLocale());
        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.FORBIDDEN,message,error);
        return new ResponseEntity<ApiError>(apiError, HttpStatus.FORBIDDEN);

    }


    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<ApiError> handleIdNotFoundException(IdNotFoundException exception){

        String message = messageSource.getMessage("id.not.found",null,"message",LocaleContextHolder.getLocale());
        String error =messageSource.getMessage("id.not.found",null,"message",LocaleContextHolder.getLocale());

        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.NOT_FOUND,message,error);
        return new ResponseEntity<ApiError>(apiError, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ApiError> handleMalformedJwtException(MalformedJwtException exception){

        String message = messageSource.getMessage("token.not.valid",null,"message",LocaleContextHolder.getLocale());
        String error =messageSource.getMessage("token.not.valid",null,"message",LocaleContextHolder.getLocale());

        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.UNAUTHORIZED,message,error);
        return new ResponseEntity<ApiError>(apiError, HttpStatus.UNAUTHORIZED);

    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ApiError> handleFileNotFoundException(FileNotFoundException exception){

        String message = messageSource.getMessage("id.not.found",null,"message",LocaleContextHolder.getLocale());
        String error =messageSource.getMessage("id.not.found",null,"message",LocaleContextHolder.getLocale());

        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.NOT_FOUND,message,error);
        return new ResponseEntity<ApiError>(apiError, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(CategoryHierarchyException.class)
    public ResponseEntity<ApiError> handleCategoryHierarchyException(CategoryHierarchyException exception){

        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.INTERNAL_SERVER_ERROR,exception.getMessage(),exception.getMessage());
        return new ResponseEntity<ApiError>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(UniqueFieldException.class)
    public ResponseEntity<ApiError> handleUniqueFieldException(UniqueFieldException exception){

        String message = messageSource.getMessage("unique.constraint.failed",null,"message",LocaleContextHolder.getLocale());

        ApiError apiError = new ApiError(LocalDateTime.now(),HttpStatus.BAD_REQUEST,message,exception.getMessage());
        return new ResponseEntity<ApiError>(apiError, HttpStatus.BAD_REQUEST);

    }



}
