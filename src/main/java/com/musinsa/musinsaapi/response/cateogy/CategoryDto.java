package com.musinsa.musinsaapi.response.cateogy;

import com.musinsa.musinsaapi.domain.Category;
import lombok.Getter;

@Getter
public class CategoryDto {
    private Long id;
    private String name;
    private Long categoryLevel;
    private Long parentId;

    public CategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.categoryLevel = category.getCategoryLevel();
        this.parentId = null;
        if (category.getParent() != null) {
            this.parentId = category.getParent().getId();
        }
    }
}
