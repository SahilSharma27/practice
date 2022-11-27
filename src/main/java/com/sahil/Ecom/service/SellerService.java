package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.FetchSellerDTO;
import com.sahil.Ecom.dto.SellerDTO;
import com.sahil.Ecom.dto.SellerProfileDTO;
import com.sahil.Ecom.entity.Seller;

public interface SellerService {

    Seller getSellerById(Long id);

    boolean register(SellerDTO sellerDTO);

    boolean checkSellerCompanyName(String companyName);

    boolean checkSellerGst(String gst);


    FetchSellerDTO fetchSellerProfileDetails(String userEmail);

    boolean updateSellerProfile(String username, SellerProfileDTO sellerProfileDTO);
}
