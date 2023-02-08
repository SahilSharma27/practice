package com.sahil.ecom.service;

import com.sahil.ecom.dto.*;
import com.sahil.ecom.dto.category.FetchCategoryDTO;
import com.sahil.ecom.dto.seller.AddSellerDTO;
import com.sahil.ecom.dto.seller.SellerProfileDTO;
import com.sahil.ecom.dto.seller.SellerProfileUpdateDTO;
import com.sahil.ecom.entity.Seller;

import java.util.List;

public interface SellerService {

    Seller getSellerById(Long id);

    boolean register(AddSellerDTO addSellerDTO);

    LoginResponseDTO loginSeller(LoginRequestDTO loginRequestDTO) throws Exception;

    boolean checkSellerCompanyName(String companyName);

    boolean checkSellerGst(String gst);

    SellerProfileDTO fetchSellerProfileDetails(String userEmail);

    boolean updateSellerProfile(String username, SellerProfileUpdateDTO sellerProfileUpdateDTO);

    List<FetchCategoryDTO> getAllCategoriesForSeller();
}
