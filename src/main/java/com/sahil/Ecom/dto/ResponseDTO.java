package com.sahil.Ecom.dto;

import java.util.Date;

public class ResponseDTO {
    private Date timestamp;
    private String message;
    private int responseStatusCode;

    public ResponseDTO() {
    }

    public ResponseDTO(Date timestamp, String message, int responseStatusCode) {
        this.timestamp = timestamp;
        this.message = message;
        this.responseStatusCode = responseStatusCode;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getResponseStatusCode() {
        return responseStatusCode;
    }

    public void setResponseStatusCode(int responseStatusCode) {
        this.responseStatusCode = responseStatusCode;
    }
}
