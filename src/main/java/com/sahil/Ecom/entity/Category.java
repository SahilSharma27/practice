package com.sahil.Ecom.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "CATEGORY")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long Id;

    @Column(name = "NAME")
    private String name;

    @OneToOne
    @JoinColumn(name = "PARENT_ID")
    private Category parent;

    @JsonIgnore
    @OneToMany(mappedBy = "parent",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Set<Category> children = new HashSet<>();

    @OneToMany(mappedBy = "category",cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    private List<CategoryMetaDataFieldValue> categoryMetaDataFieldValueList = new ArrayList<>();

    public Category() {
    }

    public Category(String name) {

        super();
        this.name = name;
    }

    public Category(String name, Category parent) {
        super();
        this.name = name;
        this.parent = parent;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
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

    public void addChildren(Category category){
        this.children.add(category);
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
