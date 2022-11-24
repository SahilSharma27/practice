package com.sahil.Ecom.exception;

public class AccountNotActiveException extends RuntimeException{
    public AccountNotActiveException(String message) {
        super(message);
    }
}
