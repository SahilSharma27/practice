package com.sahil.ecom.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class CategoryFieldValueKey implements Serializable {

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "field_id")
    private Long fieldId;


    public CategoryFieldValueKey(Long categoryId, Long fieldId) {
        this.categoryId = categoryId;
        this.fieldId = fieldId;
    }


}
