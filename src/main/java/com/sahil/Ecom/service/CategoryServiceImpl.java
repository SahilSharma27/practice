package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.*;
import com.sahil.Ecom.entity.Category;
import com.sahil.Ecom.entity.CategoryFieldValueKey;
import com.sahil.Ecom.entity.CategoryMetaDataField;
import com.sahil.Ecom.entity.CategoryMetaDataFieldValue;
import com.sahil.Ecom.exception.CategoryHierarchyException;
import com.sahil.Ecom.exception.IdNotFoundException;
import com.sahil.Ecom.exception.UniqueCategoryException;
import com.sahil.Ecom.repository.CategoryMetaDataFieldRepository;
import com.sahil.Ecom.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    CategoryMetaDataFieldRepository categoryMetaDataFieldRepository;

    @Autowired
    CategoryRepository categoryRepository;

    Logger logger= LoggerFactory.getLogger(CategoryServiceImpl.class);

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

        checkCategoryUniqueness(addCategoryDTO);

        if(addCategoryDTO.getParentId()!=null) {

            Category parent = categoryRepository
                    .findById(addCategoryDTO.getParentId())
                    .orElseThrow(IdNotFoundException::new);

            newCategory = new Category(addCategoryDTO.getCategoryName(),parent);

            parent.addChildren(newCategory);

            return new AddCategoryDTO(categoryRepository.save(parent));

//            Category savedCategory = categoryRepository.findByName(addCategoryDTO.getCategoryName()).get();
//
//            return new AddCategoryDTO(savedCategory);

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


    @Override
    public void addCategoryMetadataFieldWithValue(AddCategoryMetaDataFieldValueDTO addCategoryMetaDataFieldValueDTO) {

        Category category = categoryRepository
                .findById(addCategoryMetaDataFieldValueDTO
                        .getCategoryId()).orElseThrow(IdNotFoundException::new);

        CategoryMetaDataField categoryMetaDataField = categoryMetaDataFieldRepository
                .findById(addCategoryMetaDataFieldValueDTO
                        .getMetaDataFieldId())
                .orElseThrow(IdNotFoundException::new);

        String valuesString = String.join(",",addCategoryMetaDataFieldValueDTO.getMetaDataFieldValues());

        CategoryMetaDataFieldValue value = new CategoryMetaDataFieldValue(valuesString);

        value.setCategoryFieldValueKey(new CategoryFieldValueKey(category.getId(),categoryMetaDataField.getId()));

        value.setCategory(category);

        value.setCategoryMetaDataField(categoryMetaDataField);

        category.addCategoryMetaDataFieldValue(value);

        categoryMetaDataField.addCategoryMetaDataFieldValue(value);

        categoryRepository.save(category);

        categoryMetaDataFieldRepository.save(categoryMetaDataField);


    }

    @Override
    public List<FetchCategoryDTO> getAllRootCategories() {

        return categoryRepository
                .findRootCategories()
                .stream()
                .map(FetchCategoryDTO::new)
                .collect(Collectors.toList());

    }

    @Override
    public void checkCategoryUniqueness(AddCategoryDTO addCategoryDTO) {

        String categoryName = addCategoryDTO.getCategoryName();

        logger.info("----------------CHEKING ---------" + categoryName);

        //category already Exist
        if(categoryRepository.existsByName(categoryName)){

            //if we want category at root
            if(addCategoryDTO.getParentId()==null) {
                //check uniqueness at root level
                //if >0 not unique
                logger.info("--------------Parent null----------");
                if(categoryRepository.checkUniqueAtRoot(categoryName) == 0){
                    return;
                }
                throw new CategoryHierarchyException("Category: " + categoryName + "Already Exist at Root Level");
            }

            //check parents name till root
            //check all children
            Category category = categoryRepository.findById(addCategoryDTO.getParentId()).get();

            for (Category child:category.getChildren()) {
                if(child.getName().equals(categoryName)){
                    logger.info("--------------SAME SIBLING----------");
//                    return false;
                    throw new CategoryHierarchyException("SAME SIBLING ALREADY EXIST");
                }
            }

            while(category.getParent()!=null){

                if(category.getParent().getName().equals(categoryName)){
                    logger.info("--------------SAME NAME IN ROOT to NODE Path----------");
//                    return false;
                    throw  new CategoryHierarchyException("SAME NAME IN ROOT TO NODE PATH");
                }
                category = category.getParent();

            }
            logger.info("--------------ALL GOOD----------");

            return;

        }

        logger.info("--------------SIMPLE CASE----------");

//        return;
    }
}
