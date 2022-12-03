package com.sahil.Ecom.repository;

import com.sahil.Ecom.entity.Category;
import com.sahil.Ecom.entity.Product;
import com.sahil.Ecom.entity.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    boolean existsByName(String productName);

    Page<Product> findAllBySeller(Seller seller, Pageable pageable);

    Page<Product> findAllByCategory(Category category, Pageable pageable);

}
