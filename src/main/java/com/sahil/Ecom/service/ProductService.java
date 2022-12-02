package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.product.AddProductDTO;
import com.sahil.Ecom.dto.product.variation.AddProductVariationDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    void addProduct(AddProductDTO addProductDTO, String username);

    void addProductVariation(AddProductVariationDTO addProductVariationDTO, MultipartFile file);

    boolean saveProductImage(Long id, MultipartFile image);
}
