package com.sahil.ecom.repository;

import com.sahil.ecom.entity.CategoryFieldValueKey;
import com.sahil.ecom.entity.CategoryMetaDataFieldValue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryMetaDataFieldValueRepository extends JpaRepository<CategoryMetaDataFieldValue, CategoryFieldValueKey> {


//    @Query("")
//    public int updateValues(String newValues);

}
