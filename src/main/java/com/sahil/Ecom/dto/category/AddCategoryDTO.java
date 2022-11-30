package com.sahil.Ecom.dto.category;
import com.sahil.Ecom.entity.Category;

public class AddCategoryDTO {

    //Reusing it to show response
    //id is set when category added successfully
    private Long parentId;
    private String categoryName;


    public AddCategoryDTO() {
    }

    public AddCategoryDTO(Category category) {
        this.categoryName =category.getName();
    }

    public AddCategoryDTO(Long parentId, String categoryName) {
        this.parentId = parentId;
        this.categoryName = categoryName;
    }

    public AddCategoryDTO(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}

