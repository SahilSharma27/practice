package com.sahil.ecom.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.util.Date;

/**
 * The type Error response dto.
 *
 * @param <T> the type parameter
 */
@Data
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponseDto<T> implements ResponseDto<T> {

    private Long timestamp;
    private String error;
    private String message;
    private Integer code;
    private Integer status;
    private T data;

    /**
     * Instantiates a new Error response dto.
     *
     * @param data       the data
     * @param code       the code
     * @param httpStatus the http status
     */
    public ErrorResponseDto(T data, Integer code, HttpStatus httpStatus) {
        this.data = data;
        this.status = httpStatus.value();
        this.code = code;
        this.error = httpStatus.getReasonPhrase();
        this.timestamp = new Date().getTime();
    }

}
