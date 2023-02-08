package com.sahil.ecom.dto.category;

import com.sahil.ecom.entity.Category;
import com.sahil.ecom.entity.CategoryMetaDataFieldValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class FetchCategoryDTO {
    private Long id;
    private String name;
    private Category parent;
    private Set<Category> children;
    private List<CategoryMetaDataFieldValue> metaDataFieldValues;

    public FetchCategoryDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.parent = category.getParent();
        this.children = category.getChildren();
        this.metaDataFieldValues= category.getCategoryMetaDataFieldValueList();
    }
}
