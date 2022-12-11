package com.sahil.Ecom.dto.product.variation;

import org.json.simple.JSONObject;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AddProductVariationDTO {


    @NotNull(message = "{not.null}")
    private Long productId;

    @NotNull(message = "{not.null}")
    private JSONObject metadata;

    @NotNull(message = "{not.null}")
    private int quantityAvailable;

    @NotNull(message = "{not.null}")
    private double price;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public JSONObject getMetadata() {
        return metadata;
    }

    public void setMetadata(JSONObject metadata) {
        this.metadata = metadata;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
