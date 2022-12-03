package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.product.AddProductDTO;
import com.sahil.Ecom.dto.product.FetchProductCustomerDTO;
import com.sahil.Ecom.dto.product.FetchProductSellerDTO;
import com.sahil.Ecom.dto.product.variation.AddProductVariationDTO;
import com.sahil.Ecom.dto.product.variation.FetchProductVariationSellerDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    void addProduct(AddProductDTO addProductDTO, String username);

    void addProductVariation(AddProductVariationDTO addProductVariationDTO, MultipartFile file);

    boolean saveProductImage(Long id, MultipartFile image);

    List<FetchProductSellerDTO> getAllProductsForSeller(String sellerEmail, int page, int size, String sort, String order);

    List<FetchProductVariationSellerDTO> getAllProductVariationsForSeller(String sellerEmail, Long productId, int page, int size, String sort, String order);

    FetchProductSellerDTO getProductForSeller(String username, Long productId);

    FetchProductVariationSellerDTO getProductVariationForSeller(String sellerEmail,Long productId);

    List<FetchProductCustomerDTO> getAllProductsForCustomer(Long categoryId,int page, int size, String sort, String order);

    FetchProductCustomerDTO getProductForCustomer(Long productId);

}
