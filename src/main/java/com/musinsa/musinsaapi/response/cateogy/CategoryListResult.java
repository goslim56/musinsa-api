package com.musinsa.musinsaapi.response.cateogy;

import lombok.Getter;

import java.util.List;

@Getter
public class CategoryListResult<T> {
    private final List<T> categories;

    public CategoryListResult(List<T> categories) {
        this.categories = categories;
    }
}
