package com.sahil.ecom.dto.category;

import com.sahil.ecom.entity.Category;
import com.sahil.ecom.entity.CategoryMetaDataFieldValue;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FetchCategoryDTO {
    private Long id;
    private String name;
    private Category parent;
    private Set<Category> children;
    private List<CategoryMetaDataFieldValue> metaDataFieldValues;

    public static FetchCategoryDTO convertCategoryToFetchCategoryDTO(Category category) {
        return FetchCategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .parent(category.getParent())
                .children(category.getChildren())
                .metaDataFieldValues(category.getCategoryMetaDataFieldValueList())
                .build();
    }
}
