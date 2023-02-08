package com.sahil.ecom.dto.product;

import com.sahil.ecom.entity.Category;
import com.sahil.ecom.entity.Product;

public class FetchProductSellerDTO {

    private Long id;
    private String name;
    private String description;
    private String brand;
    private boolean isCancellable;
    private boolean isReturnable;
    private boolean isActive;
    private boolean isDeleted;
    private Category category;
//    private ProductVariation productVariation;

    public FetchProductSellerDTO() {
    }

    public FetchProductSellerDTO(Product product) {

        this.id = product.getId();
        this.name=product.getName();
        this.brand = product.getBrand();
        this.category = product.getCategory();
        this.description = product.getDescription();
        this.isActive = product.isActive();
        this.isCancellable = product.isCancellable();
        this.isReturnable = product.isReturnable();
        this.isDeleted = product.isDeleted();
//        this.productVariation = product.getProductVariations();

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
