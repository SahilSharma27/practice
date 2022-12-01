package com.sahil.Ecom.repository;

import com.sahil.Ecom.entity.CategoryFieldValueKey;
import com.sahil.Ecom.entity.CategoryMetaDataFieldValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryMetaDataFieldValueRepository extends JpaRepository<CategoryMetaDataFieldValue, CategoryFieldValueKey> {


//    @Query("")
//    public int updateValues(String newValues);

}
