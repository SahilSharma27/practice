package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.AddCategoryDTO;
import com.sahil.Ecom.dto.AddMetaDataFieldDTO;
import com.sahil.Ecom.dto.FetchCategoryDTO;
import com.sahil.Ecom.dto.FetchMetaDataFieldDTO;
import com.sahil.Ecom.entity.Category;
import com.sahil.Ecom.entity.CategoryMetaDataField;
import com.sahil.Ecom.exception.IdNotFoundException;
import com.sahil.Ecom.repository.CategoryMetaDataFieldRepository;
import com.sahil.Ecom.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    CategoryMetaDataFieldRepository categoryMetaDataFieldRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public AddMetaDataFieldDTO addCategoryMetadataField(AddMetaDataFieldDTO metaDataFieldDTO) {


        CategoryMetaDataField savedCategoryMetaDataField =
                categoryMetaDataFieldRepository.save(new CategoryMetaDataField(metaDataFieldDTO.getFieldName()));

        if(savedCategoryMetaDataField!=null){
            metaDataFieldDTO.setId(savedCategoryMetaDataField.getId());
        }

        return metaDataFieldDTO;

    }

    @Override
    public List<FetchMetaDataFieldDTO> getAllMetaDataFields() {

        return categoryMetaDataFieldRepository.findAll().stream().map(categoryMetaDataField -> {
             return new FetchMetaDataFieldDTO(categoryMetaDataField.getName());
        }).collect(Collectors.toList());

    }

    @Override
    public AddCategoryDTO addCategory(AddCategoryDTO addCategoryDTO) {

        Category newCategory;

        if(addCategoryDTO.getParentId()!=null) {

            Category parent = categoryRepository.findById(addCategoryDTO.getParentId())
                    .orElseThrow(IdNotFoundException::new);

            newCategory = new Category(addCategoryDTO.getCategoryName(),parent);

            parent.addChildren(newCategory);

            categoryRepository.save(parent);

            Category savedCategory = categoryRepository.findByName(addCategoryDTO.getCategoryName()).get();

            return new AddCategoryDTO(savedCategory);

        }

        return  new AddCategoryDTO(categoryRepository.save(new Category(addCategoryDTO.getCategoryName())));

    }

    @Override
    public List<FetchCategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream().map(FetchCategoryDTO::new).collect(Collectors.toList());
    }

    @Override
    public FetchCategoryDTO getCategoryById(Long id) {

        Category category = categoryRepository
                .findById(id)
                .orElseThrow(IdNotFoundException::new);

        return new FetchCategoryDTO(category);
    }
}
