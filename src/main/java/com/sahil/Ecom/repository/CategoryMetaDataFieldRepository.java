package com.sahil.Ecom.repository;

import com.sahil.Ecom.entity.CategoryMetaDataField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryMetaDataFieldRepository extends JpaRepository<CategoryMetaDataField,Long> {

    Optional<CategoryMetaDataField> findByName(String name);
}
