package com.sahil.ecom.dto.category.metadata.field.value;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddCategoryMetaDataFieldValueDTO {

    @NotNull(message = "{not.null}")
    private Long categoryId;

    @NotNull(message = "{not.null}")
    private Long metaDataFieldId;

    @NotNull(message = "{not.null}")
    private Set<String> metaDataFieldValues;

}
