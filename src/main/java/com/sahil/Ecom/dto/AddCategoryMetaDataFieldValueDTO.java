package com.sahil.Ecom.dto;

import java.util.Set;

public class AddCategoryMetaDataFieldValueDTO {

    private Long categoryId;
    private Long metaDataFieldId;
    private Set<String> metaDataFieldValues;

    public AddCategoryMetaDataFieldValueDTO() {
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getMetaDataFieldId() {
        return metaDataFieldId;
    }

    public void setMetaDataFieldId(Long metaDataFieldId) {
        this.metaDataFieldId = metaDataFieldId;
    }

    public Set<String> getMetaDataFieldValues() {
        return metaDataFieldValues;
    }

    public void setMetaDataFieldValues(Set<String> metaDataFieldValues) {
        this.metaDataFieldValues = metaDataFieldValues;
    }
}
