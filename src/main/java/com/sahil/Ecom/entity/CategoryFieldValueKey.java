package com.sahil.Ecom.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class CategoryFieldValueKey implements Serializable {

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "field_id")
    private Long fieldId;

    public CategoryFieldValueKey() {
    }

    public CategoryFieldValueKey(Long categoryId, Long fieldId) {
        this.categoryId = categoryId;
        this.fieldId = fieldId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getFieldId() {
        return fieldId;
    }

    public void setFieldId(Long fieldId) {
        this.fieldId = fieldId;
    }
}
