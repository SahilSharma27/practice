package com.sahil.Ecom.dto;

public class FetchMetaDataFieldDTO {

    private String fieldName;

    public FetchMetaDataFieldDTO() {
    }

    public FetchMetaDataFieldDTO(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
