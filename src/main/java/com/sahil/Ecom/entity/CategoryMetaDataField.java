package com.sahil.Ecom.entity;

import javax.persistence.*;

@Entity
@Table(name = "CATEGORY_METADATA_FIELD")
public class CategoryMetaDataField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME" , unique = true)
    private String name;


    public CategoryMetaDataField(String name) {
        this.name = name;
    }

    public CategoryMetaDataField() {
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
}
