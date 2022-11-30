package com.sahil.Ecom.dto.category;

import javax.validation.constraints.NotBlank;

public class AddMetaDataFieldDTO {

    private Long id;

    @NotBlank
    private String fieldName;

    public AddMetaDataFieldDTO() {
    }

    public AddMetaDataFieldDTO(String fieldName) {
        this.fieldName = fieldName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}
