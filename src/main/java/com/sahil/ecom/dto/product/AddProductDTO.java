package com.sahil.ecom.dto.product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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



}
