package com.sahil.Ecom.repository;

import com.sahil.Ecom.entity.Category;
import com.sahil.Ecom.entity.Product;
import com.sahil.Ecom.entity.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    boolean existsByName(String productName);

    Page<Product> findAllBySeller(Seller seller, Pageable pageable);

    Page<Product> findAllByCategory(Category category, Pageable pageable);

    @Modifying
    @Query("UPDATE Product SET isActive =:isActive WHERE id =:id")
    public int setProductActive(@Param("isActive") boolean isActive, @Param("id") Long id);


}
