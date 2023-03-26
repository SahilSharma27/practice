package com.sahil.ecom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private boolean success;
    private String message;
    private HttpStatus responseStatusCode;

    public ResponseDTO(LocalDateTime timestamp, boolean success, HttpStatus responseStatusCode) {
        this.timestamp = timestamp;
        this.success = success;
        this.responseStatusCode = responseStatusCode;
    }
}

