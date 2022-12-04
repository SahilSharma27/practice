package com.sahil.Ecom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Date;

public class ResponseDTO {

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private boolean success;
    private String message;
    private HttpStatus responseStatusCode;


    public ResponseDTO() {
    }

    public ResponseDTO(LocalDateTime timestamp, boolean success, HttpStatus responseStatusCode) {
        this.timestamp = timestamp;
        this.success = success;
        this.responseStatusCode = responseStatusCode;
    }

    public ResponseDTO(LocalDateTime timestamp, boolean success, String message, HttpStatus responseStatusCode) {
        this.timestamp = timestamp;
        this.success = success;
        this.message = message;
        this.responseStatusCode = responseStatusCode;
    }


    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getResponseStatusCode() {
        return responseStatusCode;
    }

    public void setResponseStatusCode(HttpStatus responseStatusCode) {
        this.responseStatusCode = responseStatusCode;
    }
}

