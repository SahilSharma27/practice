package com.sahil.ecom.dto.category.metadata.field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddMetaDataFieldDTO {

    private Long id;

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    private String fieldName;

    public AddMetaDataFieldDTO(String fieldName) {
        this.fieldName = fieldName;
    }

}
