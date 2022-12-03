package com.sahil.Ecom.dto.product.variation;

import com.sahil.Ecom.entity.ProductVariation;
import org.json.simple.JSONObject;

public class FetchProductVariationSellerDTO {

    private Long id;

    private int quantityAvailable;

    private double price;

    private JSONObject metadata;

    private boolean isActive;

    private String primaryImageURL;


//    private Product product;

    public FetchProductVariationSellerDTO() {
    }

    public FetchProductVariationSellerDTO(ProductVariation productVariation) {
        this.id = productVariation.getId();
        this.quantityAvailable = productVariation.getQuantityAvailable();
        this.price = productVariation.getPrice();
        this.metadata = productVariation.getMetadata();
        this.isActive = productVariation.isActive();

        String url = "localhost:8080/images/product/";

        this.setPrimaryImageURL(url + this.id + ".jpeg");

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public JSONObject getMetadata() {
        return metadata;
    }

    public void setMetadata(JSONObject metadata) {
        this.metadata = metadata;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getPrimaryImageURL() {
        return primaryImageURL;
    }

    public void setPrimaryImageURL(String primaryImageURL) {
        this.primaryImageURL = primaryImageURL;
    }

    //    public Product getProduct() {
//        return product;
//    }
//
//    public void setProduct(Product product) {
//        this.product = product;
//    }
}
