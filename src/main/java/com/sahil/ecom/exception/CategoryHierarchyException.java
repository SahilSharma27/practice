package com.sahil.ecom.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CategoryHierarchyException extends RuntimeException{

    public CategoryHierarchyException() {
    }

    public CategoryHierarchyException(String message) {
        super(message);
    }
}
