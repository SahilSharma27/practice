package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.AddCategoryDTO;
import com.sahil.Ecom.dto.AddMetaDataFieldDTO;
import com.sahil.Ecom.dto.FetchCategoryDTO;
import com.sahil.Ecom.dto.FetchMetaDataFieldDTO;
import com.sahil.Ecom.entity.Category;


import java.util.List;

public interface CategoryService {

    AddMetaDataFieldDTO addCategoryMetadataField(AddMetaDataFieldDTO metaDataFieldDTO);

    List<FetchMetaDataFieldDTO> getAllMetaDataFields();

    AddCategoryDTO addCategory(AddCategoryDTO addCategoryDTO);

    List<FetchCategoryDTO> getAllCategories();

    FetchCategoryDTO getCategoryById(Long id);
}
