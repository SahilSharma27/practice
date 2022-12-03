package com.sahil.Ecom.dto.product;

import com.sahil.Ecom.dto.product.variation.FetchProductVariationSellerDTO;
import com.sahil.Ecom.entity.Category;
import com.sahil.Ecom.entity.Product;
import com.sahil.Ecom.entity.ProductVariation;

import java.util.List;
import java.util.stream.Collectors;

public class FetchProductCustomerDTO {

    private String name;
    private String description;
    private String brand;
    private boolean isCancellable;
    private boolean isReturnable;
    private boolean isActive;
    private Category category;
    private List<FetchProductVariationSellerDTO> productVariations;


    public FetchProductCustomerDTO() {
    }

    public FetchProductCustomerDTO(Product savedProduct) {
        this.name = savedProduct.getName();
        this.description = savedProduct.getDescription();
        this.brand = savedProduct.getBrand();
        this.isCancellable = savedProduct.isCancellable();
        this.isReturnable = savedProduct.isReturnable();
        this.isActive = savedProduct.isActive();
        this.category = savedProduct.getCategory();

        this.productVariations =

                savedProduct.getProductVariations()
                        .stream()
                        .map(FetchProductVariationSellerDTO::new)
                        .collect(Collectors.toList());



    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public boolean isCancellable() {
        return isCancellable;
    }

    public void setCancellable(boolean cancellable) {
        isCancellable = cancellable;
    }

    public boolean isReturnable() {
        return isReturnable;
    }

    public void setReturnable(boolean returnable) {
        isReturnable = returnable;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

//    public List<ProductVariation> getProductVariations() {
//        return productVariations;
//    }

//    public void setProductVariations(List<ProductVariation> productVariations) {
//        this.productVariations = productVariations;
//    }

    public void setProductVariations(List<FetchProductVariationSellerDTO> productVariations) {
        this.productVariations = productVariations;
    }

    public List<FetchProductVariationSellerDTO> getProductVariations() {
        return productVariations;
    }
    //    public String getPrimaryImageURL() {
//        return primaryImageURL;
//    }
//
//    public void setPrimaryImageURL(String primaryImageURL) {
//        this.primaryImageURL = primaryImageURL;
//    }
}
