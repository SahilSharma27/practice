package com.sahil.Ecom.repository;

import com.sahil.Ecom.entity.Customer;
import com.sahil.Ecom.entity.Seller;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller,Long> {
    boolean existsByCompanyName(String companyName);
    boolean existsByGst(String gst);

    Optional<Seller> findByEmail(String userEmail);
}
