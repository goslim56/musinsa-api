package com.musinsa.musinsaapi.service;

import com.musinsa.musinsaapi.domain.Category;
import com.musinsa.musinsaapi.exception.CategoryLoopException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;

    @Test
    void saveTest() {
        Category category = new Category();
        category.setName("TEST");
        assertThat(category.getId()).isNull();
        categoryService.save(category);
        assertThat(category.getId()).isNotNull();
        assertThat(category.getCategoryLevel()).isEqualTo(0L);
    }

    @Test
    void setParentTest() {
        try {
            Category parentCategory = new Category();
            parentCategory.setName("Parent");

            Category childrenCategory1 = new Category();
            childrenCategory1.setName("Children1");
            childrenCategory1.setParent(parentCategory);

            Category childrenCategory2 = new Category();
            childrenCategory2.setName("Children2");
            childrenCategory2.setParent(parentCategory);

            Category grandchildCategor = new Category();
            grandchildCategor.setName("Grandchild");
            grandchildCategor.setParent(childrenCategory2);

            categoryService.save(parentCategory);
            categoryService.save(childrenCategory1);
            categoryService.save(childrenCategory2);
            categoryService.save(grandchildCategor);

            //expected: parentCategory -> childrenCategory1
            //expected: parentCategory -> childrenCategory2 -> grandchildCategor

            assertThat(parentCategory).isEqualTo(childrenCategory1.getParent());
            assertThat(parentCategory).isEqualTo(childrenCategory2.getParent());
            assertThat(parentCategory).isEqualTo(grandchildCategor.getParent().getParent());
            assertThat(parentCategory.getCategoryLevel()).isLessThan(childrenCategory1.getCategoryLevel());
            assertThat(parentCategory.getCategoryLevel()).isLessThan(childrenCategory2.getCategoryLevel());
            assertThat(childrenCategory1.getCategoryLevel()).isSameAs(childrenCategory2.getCategoryLevel());
            assertThat(childrenCategory2.getCategoryLevel()).isLessThan(grandchildCategor.getCategoryLevel());

        } catch (CategoryLoopException e) {
            fail("Incorrect category parent setting");
        }
    }

    @Test
    void changeParentTest() {
        try {
            Category parentCategory = new Category();
            parentCategory.setName("Parent");

            Category childrenCategory1 = new Category();
            childrenCategory1.setName("Children1");
            childrenCategory1.setParent(parentCategory);

            Category childrenCategory2 = new Category();
            childrenCategory2.setName("Children2");
            childrenCategory2.setParent(parentCategory);

            Category grandchildCategor = new Category();
            grandchildCategor.setName("Grandchild");
            grandchildCategor.setParent(childrenCategory2);

            categoryService.save(parentCategory);
            categoryService.save(childrenCategory1);
            categoryService.save(childrenCategory2);
            categoryService.save(grandchildCategor);
            childrenCategory1.setParent(grandchildCategor);

            //expected: parentCategory -> childrenCategory2 -> grandchildCategor -> childrenCategory1
            assertThat(parentCategory).isEqualTo(childrenCategory2.getParent());
            assertThat(parentCategory).isEqualTo(grandchildCategor.getParent().getParent());
            assertThat(parentCategory).isEqualTo(childrenCategory1.getParent().getParent().getParent());

        } catch (CategoryLoopException e) {
            fail("Incorrect category parent setting");
        }
    }

    @Test
    void changeParentExceptionTest() {
        try {
            Category parentCategory = new Category();
            parentCategory.setName("Parent");

            Category childrenCategory = new Category();
            childrenCategory.setName("Children");
            childrenCategory.setParent(parentCategory);


            Category grandchildCategor = new Category();
            grandchildCategor.setName("Grandchild");
            grandchildCategor.setParent(childrenCategory);

            categoryService.save(parentCategory);
            categoryService.save(childrenCategory);
            categoryService.save(grandchildCategor);
            //expected: parentCategory -> childrenCategory -> grandchildCategor

            assertThrows(CategoryLoopException.class, () -> {
                parentCategory.setParent(childrenCategory);
            });
            assertThrows(CategoryLoopException.class, () -> {
                parentCategory.setParent(grandchildCategor);
            });
            assertThrows(CategoryLoopException.class, () -> {
                childrenCategory.setParent(grandchildCategor);
            });

        } catch (CategoryLoopException e) {
            fail("Incorrect category parent setting");
        }
    }


    @Test
    void findAll() {
        saveTest();
        saveTest();
        List<Category> allCategory = categoryService.findAll();
        assertThat(allCategory.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    void findAllByParentId() {
        try {
            Category parentCategory = new Category();
            parentCategory.setName("Parent");

            Category childrenCategory1 = new Category();
            childrenCategory1.setName("Children1");
            childrenCategory1.setParent(parentCategory);

            Category childrenCategory2 = new Category();
            childrenCategory2.setName("Children2");
            childrenCategory2.setParent(parentCategory);

            Category grandchildCategor = new Category();
            grandchildCategor.setName("Grandchild");
            grandchildCategor.setParent(childrenCategory2);

            categoryService.save(parentCategory);
            categoryService.save(childrenCategory1);
            categoryService.save(childrenCategory2);
            categoryService.save(grandchildCategor);

            //expected: parentCategory -> childrenCategory1
            //expected: parentCategory -> childrenCategory2 -> grandchildCategor

            List<Category> CategoryByParentId = categoryService.findAllByParentId(parentCategory.getId());
            List<Category> CategoryByChild1Id = categoryService.findAllByParentId(childrenCategory1.getId());
            List<Category> CategoryByChild2Id = categoryService.findAllByParentId(childrenCategory2.getId());
            List<Category> CategoryBygrandchildId = categoryService.findAllByParentId(grandchildCategor.getId());
            assertThat(CategoryByParentId.size()).isEqualTo(2);
            assertThat(CategoryByChild1Id.size()).isEqualTo(0);
            assertThat(CategoryByChild2Id.size()).isEqualTo(1);
            assertThat(CategoryBygrandchildId.size()).isEqualTo(0);

        } catch (CategoryLoopException e) {
            fail("Incorrect category parent setting");
        }
    }
}