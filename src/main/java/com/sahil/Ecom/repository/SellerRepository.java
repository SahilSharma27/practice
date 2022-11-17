package com.sahil.Ecom.repository;

import com.sahil.Ecom.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller,Long> {
    boolean existsByCompanyName(String companyName);
    boolean existsByGst(String gst);
}
