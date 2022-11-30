package com.sahil.Ecom.dto.category;

import com.sahil.Ecom.entity.Category;
import com.sahil.Ecom.entity.CategoryMetaDataFieldValue;

import java.util.List;
import java.util.Set;

public class FetchCategoryDTO {

    private Long id;
    private String name;
    private Category parent;
    private Set<Category> children;
    private List<CategoryMetaDataFieldValue> metaDataFieldValues;

    public FetchCategoryDTO() {
    }

    public FetchCategoryDTO(Category category) {

        this.id = category.getId();
        this.name = category.getName();
        this.parent = category.getParent();
        this.children = category.getChildren();
        this.metaDataFieldValues= category.getCategoryMetaDataFieldValueList();

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Set<Category> getChildren() {
        return children;
    }

    public void setChildren(Set<Category> children) {
        this.children = children;
    }

    public List<CategoryMetaDataFieldValue> getMetaDataFieldValues() {
        return metaDataFieldValues;
    }

    public void setMetaDataFieldValues(List<CategoryMetaDataFieldValue> metaDataFieldValues) {
        this.metaDataFieldValues = metaDataFieldValues;
    }
}
