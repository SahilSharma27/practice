package com.sahil.ecom.service;

import com.sahil.ecom.dto.product.AddProductDTO;
import com.sahil.ecom.dto.product.FetchProductCustomerDTO;
import com.sahil.ecom.dto.product.FetchProductSellerDTO;
import com.sahil.ecom.dto.product.variation.AddProductVariationDTO;
import com.sahil.ecom.dto.product.variation.FetchProductVariationSellerDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    void addProduct(AddProductDTO addProductDTO);

    void addProductVariation(AddProductVariationDTO addProductVariationDTO, MultipartFile file);

    boolean saveProductImage(Long id, MultipartFile image);

    List<FetchProductSellerDTO> getAllProductsForSeller(int page, int size, String sort, String order);

    List<FetchProductVariationSellerDTO> getAllProductVariationsForSeller(Long productId, int page, int size, String sort, String order);

    FetchProductSellerDTO getProductForSeller(Long productId);

    FetchProductVariationSellerDTO getProductVariationForSeller(Long productId);

    List<FetchProductCustomerDTO> getAllProductsForCustomer(Long categoryId, int page, int size, String sort, String order);

    FetchProductCustomerDTO getProductForCustomer(Long productId);

    void activateProduct(Long id);
}
