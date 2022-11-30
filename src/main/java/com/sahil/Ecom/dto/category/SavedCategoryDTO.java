package com.sahil.Ecom.dto.category;

import com.sahil.Ecom.entity.Category;

public class SavedCategoryDTO {
    private Long id;
    private String categoryName;

    public SavedCategoryDTO(Long id, String categoryName) {
        this.id = id;
        this.categoryName = categoryName;
    }

    public SavedCategoryDTO() {
    }

    public SavedCategoryDTO(Category category) {
        this.id= category.getId();
        this.categoryName = category.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
