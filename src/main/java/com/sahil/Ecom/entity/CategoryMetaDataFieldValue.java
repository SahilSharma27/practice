package com.sahil.Ecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sahil.Ecom.audit.Auditable;

import javax.persistence.*;

@Entity
@Table(name = "CATEGORY_METADATA_FIELD_VALUE")
public class CategoryMetaDataFieldValue{

    @EmbeddedId
    @JsonIgnore
    CategoryFieldValueKey categoryFieldValueKey = new CategoryFieldValueKey();

    @Column(name = "FIELD_VALUES")
    private String values;

    @JsonIgnore
    @ManyToOne
    @MapsId("categoryId")
    private Category category;

    @ManyToOne
    @MapsId("fieldId")
    @JsonIgnore
    private CategoryMetaDataField categoryMetaDataField;

    public CategoryMetaDataFieldValue() {
    }

    public CategoryMetaDataFieldValue(CategoryFieldValueKey categoryFieldValueKey, String values, Category category, CategoryMetaDataField categoryMetaDataField) {
        this.categoryFieldValueKey = categoryFieldValueKey;
        this.values = values;
        this.category = category;
        this.categoryMetaDataField = categoryMetaDataField;
    }

    public CategoryMetaDataFieldValue(String values) {
        this.values = values;
    }

    public CategoryFieldValueKey getCategoryFieldValueKey() {
        return categoryFieldValueKey;
    }

    public void setCategoryFieldValueKey(CategoryFieldValueKey categoryFieldValueKey) {
        this.categoryFieldValueKey = categoryFieldValueKey;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public CategoryMetaDataField getCategoryMetaDataField() {
        return categoryMetaDataField;
    }

    public void setCategoryMetaDataField(CategoryMetaDataField categoryMetaDataField) {
        this.categoryMetaDataField = categoryMetaDataField;
    }

//    @Override
//    public String toString() {
//        values.
//    }
}
