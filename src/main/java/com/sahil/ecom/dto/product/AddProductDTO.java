package com.sahil.ecom.dto.product;

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
public class AddProductDTO {

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    private String name;

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    private String brand;

    @NotNull(message = "{not.null}")
    private Long categoryId;

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    private String description;

    @NotNull(message = "{not.null}")
    private boolean isCancellable;

    @NotNull(message = "{not.null}")
    private boolean isReturnable;
}
