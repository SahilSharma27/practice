package com.sahil.ecom.service;

import com.sahil.ecom.dto.category.*;
import com.sahil.ecom.dto.category.metadata.field.AddMetaDataFieldDTO;
import com.sahil.ecom.dto.category.metadata.field.FetchMetaDataFieldDTO;
import com.sahil.ecom.dto.category.metadata.field.value.AddCategoryMetaDataFieldValueDTO;


import java.util.List;

public interface CategoryService {

    AddMetaDataFieldDTO addCategoryMetadataField(AddMetaDataFieldDTO metaDataFieldDTO);
    List<FetchMetaDataFieldDTO> getAllMetaDataFields();

    SavedCategoryDTO addCategory(AddCategoryDTO addCategoryDTO);

    boolean updateCategory(CategoryUpdateDTO categoryUpdateDTO);

    List<FetchCategoryDTO> getAllCategories();

    FetchCategoryDTO getCategoryById(Long id);

    void addCategoryMetadataFieldWithValue(AddCategoryMetaDataFieldValueDTO addCategoryMetaDataFieldValueDTO);

    void checkCategoryUniqueness(AddCategoryDTO addCategoryDTO);

    List<FetchCategoryDTO> getAllRootCategories();

    void updateCategoryMetadataFieldWithValue(AddCategoryMetaDataFieldValueDTO updateCategoryMetaDataFieldValueDTO);
}
