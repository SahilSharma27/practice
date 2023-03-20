package com.sahil.ecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "category_metadata_field_value")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryMetaDataFieldValue {

    @EmbeddedId
    @JsonIgnore
    CategoryFieldValueKey categoryFieldValueKey = new CategoryFieldValueKey();

    @Column(name = "field_value")
    private String values;

    @JsonIgnore
    @ManyToOne
    @MapsId("categoryId")
    private Category category;

    @ManyToOne
    @MapsId("fieldId")
    @JsonIgnore
    private CategoryMetaDataField categoryMetaDataField;

    public CategoryMetaDataFieldValue(String values) {
        this.values = values;
    }


}
