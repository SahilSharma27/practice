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
    private Integer status;
    private T data;


    public ErrorResponseDto(String message, Integer status) {
        this.status = status;
        this.timestamp = new Date().getTime();
        this.message = message;
    }

    public ErrorResponseDto(T data, String message, Integer status) {
        this.status = status;
        this.timestamp = new Date().getTime();
        this.message = message;
        this.data = data;
    }
}
