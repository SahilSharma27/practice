package com.sahil.ecom.dto.product;

import com.sahil.ecom.dto.product.variation.FetchProductVariationSellerDTO;
import com.sahil.ecom.entity.Category;
import com.sahil.ecom.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FetchProductCustomerDTO {
    private String name;
    private String description;
    private String brand;
    private boolean isCancellable;
    private boolean isReturnable;
    private boolean isActive;
    private Category category;
    private List<FetchProductVariationSellerDTO> productVariations;

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
                        .toList();

    }

}
