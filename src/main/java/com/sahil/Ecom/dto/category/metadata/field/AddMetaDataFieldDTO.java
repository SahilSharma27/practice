package com.sahil.Ecom.dto.category.metadata.field;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AddMetaDataFieldDTO {

    @NotNull(message = "{not.null}")
    private Long id;

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
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
