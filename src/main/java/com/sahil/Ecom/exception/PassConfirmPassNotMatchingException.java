package com.sahil.Ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Password doesn't match with confirm password")  // 404
public class PassConfirmPassNotMatchingException extends RuntimeException{
    //

}
