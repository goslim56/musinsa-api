package com.musinsa.musinsaapi.controller;

import com.musinsa.musinsaapi.domain.Category;
import com.musinsa.musinsaapi.exception.CategoryLoopException;
import com.musinsa.musinsaapi.request.category.CategoryCreate;
import com.musinsa.musinsaapi.request.category.CategoryDelete;
import com.musinsa.musinsaapi.request.category.CategoryUpdate;
import com.musinsa.musinsaapi.response.RequestResult;
import com.musinsa.musinsaapi.response.cateogy.CategoryDto;
import com.musinsa.musinsaapi.response.cateogy.CategoryListResult;
import com.musinsa.musinsaapi.response.cateogy.CategoryWithChildDto;
import com.musinsa.musinsaapi.response.dto.cateogy.Response;
import com.musinsa.musinsaapi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public ResponseEntity<CategoryListResult<CategoryDto>> getCategories() {
        List<Category> categories = categoryService.findAll();
        List<CategoryDto> categoriesDtos = categories.stream().map(CategoryDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(new CategoryListResult<>(categoriesDtos));
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryListResult<CategoryWithChildDto>> getCategory(@PathVariable(value = "id") final Long id) {
        List<Category> categories = categoryService.findAllByParentId(id);
        List<CategoryWithChildDto> categoryDtos = categories.stream().map(CategoryWithChildDto::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(new CategoryListResult<>(categoryDtos));
    }

    @PostMapping("/category")
    public ResponseEntity<Response> createCategory(@RequestBody @Valid CategoryCreate categoryCreate) {
        Category category = new Category();
        category.setName(categoryCreate.getName());
        Long parentId = categoryCreate.getParentId();
        if (parentId != null) {
            Optional<Category> parentCategoryOptional = categoryService.findById(parentId);
            if (parentCategoryOptional.isPresent()) {
                try {
                    category.setParent(parentCategoryOptional.orElse(null));
                } catch (CategoryLoopException e) {
                    return ResponseEntity.badRequest().body(new Response(RequestResult.FAIL, "This is not a valid parent category."));
                }
            } else {
                return ResponseEntity.badRequest().body(new Response(RequestResult.FAIL, "There is no parent category."));
            }
        }
        categoryService.save(category);
        return ResponseEntity.ok().body(new Response(RequestResult.SUCCESS, "category has been created."));
    }

    @PutMapping("/category")
    public ResponseEntity<Response> updateCategory(@RequestBody @Valid CategoryUpdate categoryUpdate) {
        if (categoryUpdate.getName() == null && categoryUpdate.getParentId() == null) {
            return ResponseEntity.badRequest().body(new Response(RequestResult.FAIL, "There are no values to update."));
        }

        Optional<Category> categoryOptional = categoryService.findById(categoryUpdate.getId());
        if (categoryOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new Response(RequestResult.FAIL, "There is no category."));
        }
        Category category = categoryOptional.get();

        String name = categoryUpdate.getName();
        if (name != null) {
            category.setName(name);
        }

        Long parentId = categoryUpdate.getParentId();
        if (parentId != null) {
            Optional<Category> parentCategoryOptional = categoryService.findById(parentId);
            if (parentCategoryOptional.isPresent()) {
                try {
                    category.setParent(parentCategoryOptional.get());
                } catch (CategoryLoopException e) {
                    return ResponseEntity.badRequest().body(new Response(RequestResult.FAIL, "This is not a valid parent category."));
                }
            } else {
                return ResponseEntity.badRequest().body(new Response(RequestResult.FAIL, "There is no parent category."));
            }
        }
        categoryService.save(category);
        return ResponseEntity.ok().body(new Response(RequestResult.SUCCESS, "category has been updated."));
    }

    @DeleteMapping("/category")
    public ResponseEntity<Response> deleteCategory(@RequestBody @Valid CategoryDelete categoryDelete) {
        Optional<Category> categoryOptional = categoryService.findById(categoryDelete.getId());
        if (categoryOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(new Response(RequestResult.FAIL, "There is no category."));
        }

        Category category = categoryOptional.get();
        categoryService.delete(category);
        return ResponseEntity.ok().body(new Response(RequestResult.SUCCESS, "category has been deleted."));
    }
}
