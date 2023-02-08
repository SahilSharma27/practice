package com.sahil.ecom.repository;

import com.sahil.ecom.entity.CategoryMetaDataField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryMetaDataFieldRepository extends JpaRepository<CategoryMetaDataField,Long> {

    Optional<CategoryMetaDataField> findByName(String name);

    boolean existsByName(String fieldName);
}
