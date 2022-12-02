package com.sahil.Ecom.repository;

import com.sahil.Ecom.entity.Product;
import com.sahil.Ecom.entity.ProductVariation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariationRepository extends JpaRepository<ProductVariation,Long> {

    Page<ProductVariation> findAllByProduct(Product productForSeller, Pageable pageable);
}
