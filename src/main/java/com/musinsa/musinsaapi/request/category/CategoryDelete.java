package com.musinsa.musinsaapi.request.category;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CategoryDelete {
    @NotNull
    private Long id;
}
