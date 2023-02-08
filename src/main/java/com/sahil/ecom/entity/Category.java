package com.sahil.ecom.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sahil.ecom.audit.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "CATEGORY")
@Getter
@Setter
@NoArgsConstructor
public class Category extends Auditable {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long Id;

    @Column(name = "NAME")
    private String name;

    @OneToOne
    @JoinColumn(name = "PARENT_ID")
    private Category parent;

    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<Category> children = new HashSet<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "category", cascade = CascadeType.MERGE)
    private List<CategoryMetaDataFieldValue> categoryMetaDataFieldValueList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();


    public Category(String name) {
        super();
        this.name = name;
    }

    public Category(String name, Category parent) {
        super();
        this.name = name;
        this.parent = parent;
    }

    public void addChildren(Category category) {
        this.children.add(category);
    }

    public void addProducts(Product products) {
        this.products.add(products);
    }

    public void addCategoryMetaDataFieldValue(CategoryMetaDataFieldValue fieldValue) {
        this.categoryMetaDataFieldValueList.add(fieldValue);
    }

}
