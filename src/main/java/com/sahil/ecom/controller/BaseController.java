package com.sahil.ecom.controller;

import com.sahil.ecom.dto.response.ResponseDto;
import com.sahil.ecom.enums.response.SuccessResponse;
import com.sahil.ecom.util.PrepareResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {

    @Autowired
    private PrepareResponseUtil responseUtil;

    protected ResponseDto getResponse(Object data) {
        return this.responseUtil.prepareSuccessResponse(data, SuccessResponse.REQUEST_PROCESSED);
    }

    protected ResponseDto getValidatedResponse(Object data,Object validationResponse) {
        return this.responseUtil.prepareSuccessResponse(data, SuccessResponse.REQUEST_PROCESSED);
    }
}