package com.sahil.Ecom.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sahil.Ecom.entity.Category;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

public class FetchCategoryDTO {

    private Long id;
    private String name;
    private Category parent;
    private Set<Category> children;

    public FetchCategoryDTO() {
    }

    public FetchCategoryDTO(Category category) {

        this.id = category.getId();
        this.name = category.getName();
        this.parent = category.getParent();
        this.children = category.getChildren();

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
}
