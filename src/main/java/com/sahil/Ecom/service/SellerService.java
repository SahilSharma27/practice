package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.*;
import com.sahil.Ecom.entity.Seller;

import java.util.List;

public interface SellerService {

    Seller getSellerById(Long id);

    boolean register(SellerDTO sellerDTO);

    LoginResponseDTO loginSeller(LoginRequestDTO loginRequestDTO) throws Exception;

    boolean checkSellerCompanyName(String companyName);

    boolean checkSellerGst(String gst);

    SellerProfileDTO fetchSellerProfileDetails(String userEmail);

    boolean updateSellerProfile(String username, SellerProfileUpdateDTO sellerProfileUpdateDTO);

    List<FetchCategoryDTO> getAllCategoriesForSeller();
}
