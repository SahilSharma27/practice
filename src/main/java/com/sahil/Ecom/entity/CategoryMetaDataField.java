package com.sahil.Ecom.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "CATEGORY_METADATA_FIELD")
public class CategoryMetaDataField {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME" , unique = true)
    private String name;

    @OneToMany(mappedBy = "categoryMetaDataField",cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    private List<CategoryMetaDataFieldValue> categoryMetaDataFieldValueList = new ArrayList<>();


    public CategoryMetaDataField(String name) {
        super();
        this.name = name;
    }

    public CategoryMetaDataField() {
        super();
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



    public List<CategoryMetaDataFieldValue> getCategoryMetaDataFieldValueList() {
        return categoryMetaDataFieldValueList;
    }

    public void setCategoryMetaDataFieldValueList(List<CategoryMetaDataFieldValue> categoryMetaDataFieldValueList) {
        this.categoryMetaDataFieldValueList = categoryMetaDataFieldValueList;
    }

    public void addCategoryMetaDataFieldValue(CategoryMetaDataFieldValue fieldValue) {
        this.categoryMetaDataFieldValueList.add(fieldValue);
    }
}
