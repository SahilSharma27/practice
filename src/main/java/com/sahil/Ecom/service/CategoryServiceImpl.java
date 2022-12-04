package com.sahil.Ecom.service;

import com.sahil.Ecom.dto.category.*;
import com.sahil.Ecom.dto.category.metadata.field.AddMetaDataFieldDTO;
import com.sahil.Ecom.dto.category.metadata.field.FetchMetaDataFieldDTO;
import com.sahil.Ecom.dto.category.metadata.field.value.AddCategoryMetaDataFieldValueDTO;
import com.sahil.Ecom.dto.customer.FetchCustomerDTO;
import com.sahil.Ecom.entity.*;
import com.sahil.Ecom.exception.CategoryHierarchyException;
import com.sahil.Ecom.exception.IdNotFoundException;
import com.sahil.Ecom.repository.CategoryMetaDataFieldRepository;
import com.sahil.Ecom.repository.CategoryMetaDataFieldValueRepository;
import com.sahil.Ecom.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryMetaDataFieldRepository categoryMetaDataFieldRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMetaDataFieldValueRepository categoryMetaDataFieldValueRepository;

    Logger logger= LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private MessageSource messageSource;

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
    public SavedCategoryDTO addCategory(AddCategoryDTO addCategoryDTO) {

        Category newCategory;

        checkCategoryUniqueness(addCategoryDTO);

        if(addCategoryDTO.getParentId()!=null) {

            Category parent = categoryRepository
                    .findById(addCategoryDTO.getParentId())
                    .orElseThrow(IdNotFoundException::new);

            newCategory = new Category(addCategoryDTO.getCategoryName(),parent);

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
        //
        if(categoryRepository.existsById(categoryUpdateDTO.getCategoryId())){
            return  categoryRepository
                    .updateCategoryName(
                            categoryUpdateDTO.getCategoryName(),
                            categoryUpdateDTO.getCategoryId()
                    )>0;
        }
        throw new IdNotFoundException();

    }

    public List<FetchCategoryDTO> getAllCategoriesPaged(int page, int size, String sort,String order) {

        Pageable pageable = PageRequest.of(page, size);

        if(order.equals("ASC")){

            pageable = PageRequest.of(page, size, Sort.by(sort).ascending());

        }
        else if(order.equals("DESC")){
            pageable = PageRequest.of(page, size, Sort.by(sort).descending());

        }

//        assert pageable != null;
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        List<Category> categoryList = categoryPage.getContent();

        //Constructor reference
        return categoryList.stream().map(FetchCategoryDTO::new).collect(Collectors.toList());

    }

    @Override
    public List<FetchCategoryDTO> getAllCategories() {
        return categoryRepository
                .findAll()
                .stream()
                .map(FetchCategoryDTO::new)
                .collect(Collectors.toList());
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

        //find category for given id
        Category category = categoryRepository
                .findById(addCategoryMetaDataFieldValueDTO
                        .getCategoryId()).orElseThrow(IdNotFoundException::new);

        //exception if not a leaf node
        if(category.getChildren().size() >0){
//            throw new CategoryHierarchyException(" META DATA FIELD VALUES CAN ONLY BE ADDED IN LEAF CATEGORIES ");
            throw new CategoryHierarchyException(messageSource.getMessage("leaf.category.validation",null,"message", LocaleContextHolder.getLocale()));
        }

        //find field for given id
        CategoryMetaDataField categoryMetaDataField = categoryMetaDataFieldRepository
                .findById(addCategoryMetaDataFieldValueDTO
                        .getMetaDataFieldId())
                .orElseThrow(IdNotFoundException::new);

        //make string form set
        String valuesString = String.join(",",addCategoryMetaDataFieldValueDTO.getMetaDataFieldValues());

        //set up object to save in db
        CategoryMetaDataFieldValue value = new CategoryMetaDataFieldValue(valuesString);
        value.setCategoryFieldValueKey(new CategoryFieldValueKey(category.getId(),categoryMetaDataField.getId()));
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
                        .getCategoryId()).orElseThrow(IdNotFoundException::new);

        //if not a leaf then not possible
        if(category.getChildren().size() > 0){
            throw new CategoryHierarchyException(messageSource.getMessage("leaf.category.validation",null,"message", LocaleContextHolder.getLocale()));
//            throw new CategoryHierarchyException(" META DATA FIELD VALUES CAN ONLY BE ADDED IN LEAF CATEGORIES ");
        }

        //find meta field if exist
        CategoryMetaDataField categoryMetaDataField = categoryMetaDataFieldRepository
                .findById(updateCategoryMetaDataFieldValueDTO
                        .getMetaDataFieldId())
                .orElseThrow(IdNotFoundException::new);

        // find value String for provide category and field
        CategoryMetaDataFieldValue categoryMetaDataFieldValue = categoryMetaDataFieldValueRepository
                .findById(new CategoryFieldValueKey(category.getId(),categoryMetaDataField.getId()))
                .orElseThrow(IdNotFoundException::new);

//        get saved values String
        String savedValueString  = categoryMetaDataFieldValue.getValues();

        //convert to set
        Set<String> savedValuesSet =  convertStringValuesToSet(savedValueString);

        //new Set of values
        Set<String> newValuesSet = updateCategoryMetaDataFieldValueDTO.getMetaDataFieldValues();


        //merge both sets
        savedValuesSet.addAll(newValuesSet);

        //convert back Set to String
        String updatedValuesString = String.join(",",savedValuesSet);

        categoryMetaDataFieldValue.setValues(updatedValuesString);

        //save
        categoryMetaDataFieldValueRepository.save(categoryMetaDataFieldValue);

    }

    private Set<String> convertStringValuesToSet(String values){

        String[] valueList = values.split(",");

        Set<String> valuesSet = new HashSet<>(Arrays.asList(valueList));
        logger.info(valuesSet.toString());

        return valuesSet;
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

        logger.info("----------------CHECKING ---------" + categoryName);

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

//                throw new CategoryHierarchyException("Category: " + categoryName + " Already Exist at Root Level ");
                throw new CategoryHierarchyException(categoryName +
                        messageSource.getMessage("root.category.validation",null,"message",LocaleContextHolder.getLocale()));
            }

            //check parents name till root
            //check all children
            Category category = categoryRepository.findById(addCategoryDTO.getParentId()).get();

            for (Category child:category.getChildren()) {
                if(child.getName().equals(categoryName)){
                    logger.info("--------------SAME SIBLING----------");
//                    return false;
//                    throw new CategoryHierarchyException(" SAME SIBLING ALREADY EXIST ");
                    throw new CategoryHierarchyException(
                            messageSource.getMessage("same.sibling.validation",null,"message",LocaleContextHolder.getLocale()));
                }
            }

            while(category.getParent()!=null){

                if(category.getParent().getName().equals(categoryName)){
                    logger.info("--------------SAME NAME IN ROOT to NODE Path----------");
//                    throw  new CategoryHierarchyException(" SAME NAME IN ROOT TO NODE PATH ");
                    throw new CategoryHierarchyException(
                            messageSource.getMessage("node.to.root.validation",null,"message",LocaleContextHolder.getLocale()));

                }
                category = category.getParent();

            }
            logger.info("--------------ALL GOOD----------");

            return;

        }

        logger.info("--------------SIMPLE CASE----------");

    }
}
