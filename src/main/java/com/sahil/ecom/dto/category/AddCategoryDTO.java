package com.sahil.ecom.dto.category;

import com.sahil.ecom.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class AddCategoryDTO {

    private Long parentId;

    @NotNull(message = "{not.null}")
    @NotBlank(message = "{not.blank}")
    private String categoryName;

    public AddCategoryDTO(Category category) {
        this.categoryName = category.getName();
    }

    public AddCategoryDTO(Long parentId, String categoryName) {
        this.parentId = parentId;
        this.categoryName = categoryName;
    }

    public AddCategoryDTO(String categoryName) {
        this.categoryName = categoryName;
    }

}

