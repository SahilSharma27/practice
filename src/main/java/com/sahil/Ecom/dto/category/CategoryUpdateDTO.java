package com.sahil.Ecom.dto.category;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CategoryUpdateDTO {

    @NotNull(message = "{not.null}")
    private Long categoryId;

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    private String categoryName;

    public CategoryUpdateDTO() {
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


}
