package com.sahil.ecom.util;

import com.sahil.ecom.dto.response.ErrorResponseDto;
import com.sahil.ecom.dto.response.ResponseDto;
import com.sahil.ecom.dto.response.SuccessResponseDto;
import com.sahil.ecom.enums.response.ErrorResponse;
import com.sahil.ecom.enums.response.SuccessResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.Locale;


@Component
public class PrepareResponseUtil {

    private final MessageSource messageSource;

    public PrepareResponseUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private Locale getLocale(String locale) {
        return locale != null ? new Locale(locale) : LocaleContextHolder.getLocale();
    }

    private String getMessage(String messageKey, Object... args) {
        return messageSource.getMessage(messageKey, args, LocaleContextHolder.getLocale());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public ResponseDto prepareSuccessResponse(Object data, SuccessResponse successResponse) {
        ResponseDto responseDto = new SuccessResponseDto<>(data, successResponse);
        responseDto.setMessage(getMessage(successResponse.getMessageCode(), ""));
        return responseDto;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public ResponseDto prepareValidatedSuccessResponse(Object data, Object validation, SuccessResponse successResponse) {
        ResponseDto responseDto = new SuccessResponseDto<>(data, validation, successResponse);
        responseDto.setMessage(getMessage(successResponse.getMessageCode(), ""));
        return responseDto;
    }

    /*@SuppressWarnings({"unchecked", "rawtypes"})
    public ResponseDto prepareErrorResponse(Object data, ErrorResponse errorResponse, String locale) {
        ResponseDto responseDto = new ErrorResponseDto<>(data, 0, errorResponse.getStatus());
        responseDto.setMessage(messageSource.getMessage(errorResponse.getMessageCode(), null, getLocale(locale)));
        return responseDto;
    }*/

    @SuppressWarnings({"unchecked", "rawtypes"})
    public ResponseDto exception(Exception e, ErrorResponse errorResponse) {
        ResponseDto responseDto = new ErrorResponseDto<>(e.getMessage(), 0, errorResponse.getStatus());
        responseDto.setMessage(messageSource.getMessage(errorResponse.getMessageCode(), null, getLocale(null)));
        return responseDto;
    }

    /*public ResponseDto hibernate_exception(org.hibernate.JDBCException e) {
        ResponseDto responseDto = new ErrorResponseDto(e.getMessage(), 0, HttpStatus.BAD_REQUEST);
        responseDto.setMessage(e.getMessage());
        return responseDto;
    }*/

    @SuppressWarnings({"unchecked", "rawtypes"})
    public ResponseDto exception(Exception e) {
        ResponseDto responseDto = new ErrorResponseDto(e.getMessage(), 0, HttpStatus.BAD_REQUEST);
        responseDto.setMessage(e.getMessage());
        return responseDto;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public ResponseDto exception(Throwable e) {
        ResponseDto responseDto = new ErrorResponseDto(e.getMessage(), 0, HttpStatus.INTERNAL_SERVER_ERROR);
        responseDto.setMessage(e.getMessage());
        return responseDto;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public ResponseDto exception(Exception e, BindingResult bindingResult) {
        String message = bindingResult.getAllErrors().get(0).getDefaultMessage();
        ResponseDto responseDto = new ErrorResponseDto(e.getMessage(), 0, HttpStatus.BAD_REQUEST);
        responseDto.setMessage(message);
        return responseDto;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public ResponseDto exception(ErrorResponseDto errorResponse) {
        return errorResponse;
    }


}

