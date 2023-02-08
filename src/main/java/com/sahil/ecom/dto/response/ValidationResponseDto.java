package com.sahil.ecom.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

/**
 * The type Success response dto.
 *
 */
@Data
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ValidationResponseDto {

    String message;
    Integer errorCode = 1;

    /**
     *
     * @param message
     * @param errorCode
     */
    public ValidationResponseDto(String message, Integer errorCode) {
        this.message = message;
        this.errorCode = errorCode;
    }

}
