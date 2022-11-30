package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.*;
import com.sahil.Ecom.entity.Category;


import java.util.List;

public interface CategoryService {

    AddMetaDataFieldDTO addCategoryMetadataField(AddMetaDataFieldDTO metaDataFieldDTO);

    List<FetchMetaDataFieldDTO> getAllMetaDataFields();

    AddCategoryDTO addCategory(AddCategoryDTO addCategoryDTO);

    List<FetchCategoryDTO> getAllCategories();

    FetchCategoryDTO getCategoryById(Long id);

    void addCategoryMetadataFieldWithValue(AddCategoryMetaDataFieldValueDTO addCategoryMetaDataFieldValueDTO);

    void checkCategoryUniqueness(AddCategoryDTO addCategoryDTO);

    List<FetchCategoryDTO> getAllRootCategories();


}
