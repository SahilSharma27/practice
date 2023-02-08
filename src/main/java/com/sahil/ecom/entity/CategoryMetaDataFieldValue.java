package com.sahil.ecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "CATEGORY_METADATA_FIELD_VALUE")
@Getter
@Setter
@NoArgsConstructor
public class CategoryMetaDataFieldValue {

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

    public CategoryMetaDataFieldValue(CategoryFieldValueKey categoryFieldValueKey, String values, Category category, CategoryMetaDataField categoryMetaDataField) {
        this.categoryFieldValueKey = categoryFieldValueKey;
        this.values = values;
        this.category = category;
        this.categoryMetaDataField = categoryMetaDataField;
    }

    public CategoryMetaDataFieldValue(String values) {
        this.values = values;
    }


}
