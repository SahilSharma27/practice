package com.sahil.ecom.entity;

import com.sahil.ecom.audit.Auditable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CATEGORY_METADATA_FIELD")
@Setter
@Getter
public class CategoryMetaDataField extends Auditable {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", unique = true)
    private String name;

    @OneToMany(mappedBy = "categoryMetaDataField", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<CategoryMetaDataFieldValue> categoryMetaDataFieldValueList = new ArrayList<>();

    public CategoryMetaDataField(String name) {
        super();
        this.name = name;
    }

    public CategoryMetaDataField() {
        super();
    }

    public void addCategoryMetaDataFieldValue(CategoryMetaDataFieldValue fieldValue) {
        this.categoryMetaDataFieldValueList.add(fieldValue);
    }
}
