package com.sahil.ecom.dto.product.variation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.simple.JSONObject;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddProductVariationDTO {

    @NotNull(message = "{not.null}")
    private Long productId;

    @NotNull(message = "{not.null}")
    private JSONObject metadata;

    @NotNull(message = "{not.null}")
    private int quantityAvailable;

    @NotNull(message = "{not.null}")
    private double price;

}
