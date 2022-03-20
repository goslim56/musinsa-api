package com.musinsa.musinsaapi.request.category;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CategoryUpdate {
    @NotNull
    private Long id;
    private String name;
    private Long parentId;
}
