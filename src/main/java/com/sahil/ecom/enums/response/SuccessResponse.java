package com.sahil.ecom.enums.response;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@FieldDefaults(level = AccessLevel.PRIVATE)
public enum SuccessResponse {

    REQUEST_PROCESSED(HttpStatus.OK, "request.processed.successfully"),
    DATA_NOT_AVAILABLE(HttpStatus.OK, "data.not.found");

    final HttpStatus status;
    final String messageCode;

    SuccessResponse(HttpStatus status, String messageCode) {
        this.status = status;
        this.messageCode = messageCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessageCode() {
        return messageCode;
    }
}