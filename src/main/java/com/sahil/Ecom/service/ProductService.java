package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.product.AddProductDTO;
import com.sahil.Ecom.dto.product.FetchProductSellerDTO;
import com.sahil.Ecom.dto.product.variation.AddProductVariationDTO;
import com.sahil.Ecom.dto.product.variation.FetchProductVariationSellerDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    void addProduct(AddProductDTO addProductDTO, String username);

    void addProductVariation(AddProductVariationDTO addProductVariationDTO, MultipartFile file);

    boolean saveProductImage(Long id, MultipartFile image);

    List<FetchProductSellerDTO> getAllProducts(String sellerEmail,int page, int size,String sort,String order);

    List<FetchProductVariationSellerDTO> getAllProductVariations(String sellerEmail,Long productId,int page, int size, String sort, String order);

    FetchProductSellerDTO getProduct(String username, Long productId);

    FetchProductVariationSellerDTO getProductVariation(String sellerEmail, Long productId);
}
