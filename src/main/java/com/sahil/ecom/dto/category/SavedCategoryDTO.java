package com.sahil.ecom.dto.category;

import com.sahil.ecom.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SavedCategoryDTO {
    private Long id;
    private String categoryName;

    public SavedCategoryDTO(Category category) {
        this.id= category.getId();
        this.categoryName = category.getName();
    }

}
