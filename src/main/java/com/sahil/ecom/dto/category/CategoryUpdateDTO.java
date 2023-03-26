package com.sahil.ecom.dto.category;

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
public class CategoryUpdateDTO {

    @NotNull(message = "{not.null}")
    private Long categoryId;

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    private String categoryName;

}
