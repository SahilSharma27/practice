package com.sahil.ecom.service.impl;

import com.sahil.ecom.dto.category.AddCategoryDTO;
import com.sahil.ecom.dto.category.CategoryUpdateDTO;
import com.sahil.ecom.dto.category.FetchCategoryDTO;
import com.sahil.ecom.dto.category.SavedCategoryDTO;
import com.sahil.ecom.dto.category.metadata.field.AddMetaDataFieldDTO;
import com.sahil.ecom.dto.category.metadata.field.FetchMetaDataFieldDTO;
import com.sahil.ecom.dto.category.metadata.field.value.AddCategoryMetaDataFieldValueDTO;
import com.sahil.ecom.entity.Category;
import com.sahil.ecom.entity.CategoryFieldValueKey;
import com.sahil.ecom.entity.CategoryMetaDataField;
import com.sahil.ecom.entity.CategoryMetaDataFieldValue;
import com.sahil.ecom.exception.GenericException;
import com.sahil.ecom.repository.CategoryMetaDataFieldRepository;
import com.sahil.ecom.repository.CategoryMetaDataFieldValueRepository;
import com.sahil.ecom.repository.CategoryRepository;
import com.sahil.ecom.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMetaDataFieldRepository categoryMetaDataFieldRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryMetaDataFieldValueRepository categoryMetaDataFieldValueRepository;
    private final MessageSource messageSource;

    @Override
    public AddMetaDataFieldDTO addCategoryMetadataField(AddMetaDataFieldDTO metaDataFieldDTO) {
        if (categoryMetaDataFieldRepository.existsByName(metaDataFieldDTO.getFieldName())) {
            throw new GenericException(messageSource
                    .getMessage("field.already.exist", null, "message", LocaleContextHolder.getLocale()));
        }
        CategoryMetaDataField savedCategoryMetaDataField =
                categoryMetaDataFieldRepository.save(new CategoryMetaDataField(metaDataFieldDTO.getFieldName()));
        if (savedCategoryMetaDataField != null) {
            metaDataFieldDTO.setId(savedCategoryMetaDataField.getId());
        }
        return metaDataFieldDTO;
    }

    @Override
    public List<FetchMetaDataFieldDTO> getAllMetaDataFields() {

        return categoryMetaDataFieldRepository.findAll().stream().map(categoryMetaDataField -> {
            return new FetchMetaDataFieldDTO(categoryMetaDataField.getName());
        }).toList();

    }

    @Override
    public SavedCategoryDTO addCategory(AddCategoryDTO addCategoryDTO) {

        Category newCategory;

        checkCategoryUniqueness(addCategoryDTO);

        if (addCategoryDTO.getParentId() != null) {

            Category parent = categoryRepository
                    .findById(addCategoryDTO.getParentId())
                    .orElseThrow(GenericException::new);

            newCategory = new Category(addCategoryDTO.getCategoryName(), parent);

            newCategory.setParent(parent);
//            parent.addChildren(newCategory);

            Category savedCategory = categoryRepository.save(newCategory);
            return new SavedCategoryDTO(savedCategory);


        }

        return new SavedCategoryDTO(categoryRepository.save(new Category(addCategoryDTO.getCategoryName())));
    }

    @Transactional
    @Override
    public boolean updateCategory(CategoryUpdateDTO categoryUpdateDTO) {

        //check if exist
        if (categoryRepository.existsById(categoryUpdateDTO.getCategoryId())) {
            return categoryRepository
                    .updateCategoryName(
                            categoryUpdateDTO.getCategoryName(),
                            categoryUpdateDTO.getCategoryId()
                    ) > 0;
        }
        throw new GenericException();

    }

    //    @Override
    public List<FetchCategoryDTO> getAllCategoriesPaged(int page, int size, String sort, String order) {

        Pageable pageable = PageRequest.of(page, size);

        if (order.equals("ASC")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
        } else if (order.equals("DESC")) {
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        }
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        List<Category> categoryList = categoryPage.getContent();
        return categoryList.stream().map(FetchCategoryDTO::convertCategoryToFetchCategoryDTO).toList();

    }

    @Override
    public List<FetchCategoryDTO> getAllCategories() {
        return categoryRepository
                .findAll()
                .stream()
                .map(FetchCategoryDTO::convertCategoryToFetchCategoryDTO)
                .toList();
    }

    @Override
    public FetchCategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository
                .findById(id)
                .orElseThrow(GenericException::new);
        return FetchCategoryDTO.convertCategoryToFetchCategoryDTO(category);
    }

    @Override
    public void addCategoryMetadataFieldWithValue(AddCategoryMetaDataFieldValueDTO addCategoryMetaDataFieldValueDTO) {

        //find category for given id
        Category category = categoryRepository
                .findById(addCategoryMetaDataFieldValueDTO
                        .getCategoryId()).orElseThrow(GenericException::new);

        //exception if not a leaf node
        if (!category.getChildren().isEmpty()) {
            throw new GenericException(messageSource.getMessage("leaf.category.validation", null, "message", LocaleContextHolder.getLocale()));
        }

        //find field for given id
        CategoryMetaDataField categoryMetaDataField = categoryMetaDataFieldRepository
                .findById(addCategoryMetaDataFieldValueDTO
                        .getMetaDataFieldId())
                .orElseThrow(GenericException::new);

        //make string form set
        String valuesString = String.join(",", addCategoryMetaDataFieldValueDTO.getMetaDataFieldValues());

        //set up object to save in db
        CategoryMetaDataFieldValue value = new CategoryMetaDataFieldValue(valuesString);
        value.setCategoryFieldValueKey(new CategoryFieldValueKey(category.getId(), categoryMetaDataField.getId()));
        value.setCategory(category);
        value.setCategoryMetaDataField(categoryMetaDataField);

        //add value to category object
        category.addCategoryMetaDataFieldValue(value);

        //add value to Field object
        categoryMetaDataField.addCategoryMetaDataFieldValue(value);


        ///save category
        categoryRepository.save(category);

        //save meta data field
        categoryMetaDataFieldRepository.save(categoryMetaDataField);
    }

    @Override
    public void updateCategoryMetadataFieldWithValue(AddCategoryMetaDataFieldValueDTO updateCategoryMetaDataFieldValueDTO) {
        //get category if exist
        Category category = categoryRepository
                .findById(updateCategoryMetaDataFieldValueDTO
                        .getCategoryId()).orElseThrow(GenericException::new);

        //if not a leaf then not possible
        if (!category.getChildren().isEmpty()) {
            throw new GenericException(messageSource.getMessage("leaf.category.validation", null, "message", LocaleContextHolder.getLocale()));
        }

        //find meta field if exist
        CategoryMetaDataField categoryMetaDataField = categoryMetaDataFieldRepository
                .findById(updateCategoryMetaDataFieldValueDTO
                        .getMetaDataFieldId())
                .orElseThrow(GenericException::new);

        // find value String for provide category and field
        CategoryMetaDataFieldValue categoryMetaDataFieldValue = categoryMetaDataFieldValueRepository
                .findById(new CategoryFieldValueKey(category.getId(), categoryMetaDataField.getId()))
                .orElseThrow(GenericException::new);

        String savedValueString = categoryMetaDataFieldValue.getValues();
        Set<String> savedValuesSet = convertStringValuesToSet(savedValueString);
        Set<String> newValuesSet = updateCategoryMetaDataFieldValueDTO.getMetaDataFieldValues();
        savedValuesSet.addAll(newValuesSet);
        String updatedValuesString = String.join(",", savedValuesSet);
        categoryMetaDataFieldValue.setValues(updatedValuesString);
        categoryMetaDataFieldValueRepository.save(categoryMetaDataFieldValue);

    }

    private Set<String> convertStringValuesToSet(String values) {
        String[] valueList = values.split(",");
        Set<String> valuesSet = new HashSet<>(Arrays.asList(valueList));
        log.info(valuesSet.toString());
        return valuesSet;
    }

    @Override
    public List<FetchCategoryDTO> getAllRootCategories() {
        return categoryRepository
                .findRootCategories()
                .stream()
                .map(FetchCategoryDTO::convertCategoryToFetchCategoryDTO)
                .toList();
    }

    @Override
    public void checkCategoryUniqueness(AddCategoryDTO addCategoryDTO) {
        String categoryName = addCategoryDTO.getCategoryName();
        log.info("----------------CHECKING ---------" + categoryName);
        //category already Exist
        if (categoryRepository.existsByName(categoryName)) {
            //if we want category at root
            if (Objects.isNull(addCategoryDTO.getParentId())) {
                //check uniqueness at root level,if >0 not unique
                log.info("--------------Parent null----------");
                if (categoryRepository.checkUniqueAtRoot(categoryName) == 0) {
                    return;
                }
                throw new GenericException(categoryName + messageSource.getMessage("root.category.validation", null, "message", LocaleContextHolder.getLocale()));
            }
            /***
             check parents name till root
             check all children
             */
            Category category = categoryRepository.findById(addCategoryDTO.getParentId()).orElseThrow(() -> new GenericException(""));
            for (Category child : category.getChildren()) {
                if (child.getName().equals(categoryName)) {
                    log.info("--------------SAME SIBLING----------");
                    throw new GenericException(messageSource.getMessage("same.sibling.validation", null, "message", LocaleContextHolder.getLocale()));
                }
            }

            while (category.getParent() != null) {
                if (category.getParent().getName().equals(categoryName)) {
                    log.info("--------------SAME NAME IN ROOT to NODE Path----------");
                    throw new GenericException(messageSource.getMessage("node.to.root.validation", null, "message", LocaleContextHolder.getLocale()));
                }
                category = category.getParent();
            }
            return;
        }
        log.info("--------------SIMPLE CASE----------");
    }
}
