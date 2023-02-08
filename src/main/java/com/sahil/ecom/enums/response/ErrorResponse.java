package com.sahil.ecom.enums.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

/**
 * The enum Error response.
 */
@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorResponse {
    /**
     * Bad request error response.
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "bad.request"),
    /**
     * Internal server error error response.
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "internal.server.error");

    HttpStatus status;
    String messageCode;

}
