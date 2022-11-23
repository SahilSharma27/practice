package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.FetchSellerDTO;
import com.sahil.Ecom.dto.SellerDTO;
import com.sahil.Ecom.entity.Seller;

public interface SellerService {

    Seller getSellerById(Long id);

    Seller register(SellerDTO sellerDTO);

    boolean checkSellerCompanyName(String companyName);

    boolean checkSellerGst(String gst);


    FetchSellerDTO fetchSellerProfileDetails(String userEmail);
}
