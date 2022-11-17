package com.sahil.Ecom.service;

import com.sahil.Ecom.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SellerServiceImpl implements SellerService{

    @Autowired
    SellerRepository sellerRepository;

    @Override
    public boolean checkSellerCompanyName(String companyName) {
        return sellerRepository.existsByCompanyName(companyName);
    }

    @Override
    public boolean checkSellerGst(String gst) {
        return sellerRepository.existsByGst(gst);
    }
}
