package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.category.*;


import java.util.List;

public interface CategoryService {

    AddMetaDataFieldDTO addCategoryMetadataField(AddMetaDataFieldDTO metaDataFieldDTO);

    List<FetchMetaDataFieldDTO> getAllMetaDataFields();

    SavedCategoryDTO addCategory(AddCategoryDTO addCategoryDTO);

    List<FetchCategoryDTO> getAllCategories();

    FetchCategoryDTO getCategoryById(Long id);

    void addCategoryMetadataFieldWithValue(AddCategoryMetaDataFieldValueDTO addCategoryMetaDataFieldValueDTO);

    void checkCategoryUniqueness(AddCategoryDTO addCategoryDTO);

    List<FetchCategoryDTO> getAllRootCategories();


}
