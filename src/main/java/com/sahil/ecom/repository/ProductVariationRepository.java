package com.sahil.ecom.repository;

import com.sahil.ecom.entity.Product;
import com.sahil.ecom.entity.ProductVariation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariationRepository extends JpaRepository<ProductVariation,Long> {

    Page<ProductVariation> findAllByProduct(Product productForSeller, Pageable pageable);
}
