package com.sahil.ecom.dto.product.variation;

import com.sahil.ecom.entity.ProductVariation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.simple.JSONObject;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchProductVariationSellerDTO {

    private Long id;

    private int quantityAvailable;

    private double price;

    private JSONObject metadata;

    private boolean isActive;

    private String primaryImageURL;


    public FetchProductVariationSellerDTO(ProductVariation productVariation) {
        this.id = productVariation.getId();
        this.quantityAvailable = productVariation.getQuantityAvailable();
        this.price = productVariation.getPrice();
        this.metadata = productVariation.getMetadata();
        this.isActive = productVariation.isActive();

        String url = "localhost:8080/images/product/";

        this.setPrimaryImageURL(url + this.id + ".jpeg");

    }

}
