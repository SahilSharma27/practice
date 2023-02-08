package com.sahil.ecom.repository;

import com.sahil.ecom.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller,Long> {
    boolean existsByCompanyName(String companyName);
    boolean existsByGst(String gst);

    Optional<Seller> findByEmail(String userEmail);
}
