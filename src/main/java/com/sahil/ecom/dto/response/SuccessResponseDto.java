package com.sahil.ecom.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * The type Success response dto.
 *
 * @param <T> the type parameter
 */
@Data
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponseDto<T> implements ResponseDto<T> {

    String message = "Request processed successfully.";
    Integer status = 200;
    T data;

    /**
     * Instantiates a new Success response dto.
     */
    public SuccessResponseDto(T data, Integer status) {
        this.data = data;
        this.status = status;
    }

    public SuccessResponseDto(T data) {
        this.data = data;
    }

    public SuccessResponseDto(T data, String message) {
        this.data = data;
        this.message = message;
    }

}
