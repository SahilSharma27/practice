package com.sahil.ecom.dto.product;

import com.sahil.ecom.entity.Category;
import com.sahil.ecom.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    public FetchProductSellerDTO(Product product) {

        this.id = product.getId();
        this.name = product.getName();
        this.brand = product.getBrand();
        this.category = product.getCategory();
        this.description = product.getDescription();
        this.isActive = product.isActive();
        this.isCancellable = product.isCancellable();
        this.isReturnable = product.isReturnable();
        this.isDeleted = product.isDeleted();
//        this.productVariation = product.getProductVariations();

    }

}
