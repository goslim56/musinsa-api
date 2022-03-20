package com.musinsa.musinsaapi.response.cateogy;

import com.musinsa.musinsaapi.domain.Category;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CategoryWithChildDto {
    private Long id;
    private String name;
    private Long categoryLevel;
    private Long parentId;
    private List<CategoryWithChildDto> child;

    public CategoryWithChildDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.categoryLevel = category.getCategoryLevel();
        this.parentId = null;
        if (category.getParent() != null) {
            this.parentId = category.getParent().getId();
        }
        this.child = category.getChild().stream().map(CategoryWithChildDto::new).collect(Collectors.toList());
    }
}
