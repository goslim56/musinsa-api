package com.musinsa.musinsaapi.service;

import com.musinsa.musinsaapi.repository.CategoryRepository;
import com.musinsa.musinsaapi.domain.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public List<Category> findAllByParentId(Long parentId) {
        return categoryRepository.findAllByParentId(parentId);
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Transactional
    public Category save(Category category) {
        return categoryRepository.save(category);
    }


    @Transactional
    public void delete(Category category) {
        List<Category> child = category.getChild();
        for (Category children : child) {
            this.delete(children);
        }
        categoryRepository.delete(category);
    }
}
