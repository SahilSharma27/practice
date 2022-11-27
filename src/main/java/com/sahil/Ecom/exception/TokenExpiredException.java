package com.sahil.Ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)  // 404
public class TokenExpiredException extends RuntimeException{

    public TokenExpiredException() {
    }

    public TokenExpiredException(String msg){
        super(msg);
    }
}
