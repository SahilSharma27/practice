package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.SellerDTO;
import com.sahil.Ecom.entity.Seller;

public interface SellerService {

    Seller register(SellerDTO sellerDTO);

    boolean checkSellerCompanyName(String companyName);

    boolean checkSellerGst(String gst);



}
