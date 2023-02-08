package com.sahil.ecom.dto.response;


import com.sahil.ecom.enums.response.SuccessResponse;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
public class SuccessResponseDto<T> implements ResponseDto<T> {

    String message;
    Integer code = 1;
    Integer status = 200;
    T data;
    T validationData;

    /**
     * Instantiates a new Success response dto.
     *
     * @param data            the data
     * @param successResponse the success response
     */
    public SuccessResponseDto(T data, SuccessResponse successResponse) {
        this.data = data;
        this.status = successResponse.getStatus().value();
    }

    /**
     * Instantiates a new Success response dto.
     *
     * @param data            the data
     * @param successResponse the success response
     *
     */
    public SuccessResponseDto(T data,T validationData, SuccessResponse successResponse) {
        this.data = data;
        this.validationData =validationData;
        this.status = successResponse.getStatus().value();
    }

}
