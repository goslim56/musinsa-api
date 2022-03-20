package com.musinsa.musinsaapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.musinsaapi.domain.Category;
import com.musinsa.musinsaapi.repository.CategoryRepository;
import com.musinsa.musinsaapi.request.category.CategoryCreate;
import com.musinsa.musinsaapi.request.category.CategoryDelete;
import com.musinsa.musinsaapi.request.category.CategoryUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 카테고리_등록_성공_테스트() throws Exception {
        CategoryCreate categoryCreate = new CategoryCreate();
        categoryCreate.setName("테스트");

        mockMvc.perform(post("/category")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(categoryCreate)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void 카테고리_등록_실패_테스트() throws Exception {
        CategoryCreate categoryCreate = new CategoryCreate();

        mockMvc.perform(post("/category")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(categoryCreate)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    void 카테고리_수정_성공_테스트() throws Exception {
        Category category = new Category();
        category.setName("category_test");
        categoryRepository.save(category);


        String changeName = "category_put";
        CategoryUpdate categoryUpdate = new CategoryUpdate();
        categoryUpdate.setId(category.getId());
        categoryUpdate.setName(changeName);

        mockMvc.perform(put("/category")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(categoryUpdate)))
                .andDo(print())
                .andExpect(status().isOk());

        Optional<Category> categoryOptional = categoryRepository.findById(category.getId());
        if (categoryOptional.isEmpty()) {
            fail("수정 실패");
        }
        Category categoryById = categoryOptional.get();

        assertThat(categoryById.getName()).isEqualTo(changeName);
    }

    @Test
    void 카테고리_수정_실패_테스트() throws Exception {
        Category category = new Category();
        category.setName("category_test");
        categoryRepository.save(category);


        String changeName = "category_put";
        CategoryUpdate categoryUpdate = new CategoryUpdate();
        categoryUpdate.setName(changeName);

        mockMvc.perform(put("/category")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(categoryUpdate)))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        assertThat(category.getName()).isNotEqualTo(changeName);
    }

    @Test
    void 카테고리_삭제_성공_테스트() throws Exception {
        Category category = new Category();
        category.setName("category_test");
        categoryRepository.save(category);


        CategoryDelete categoryDelete = new CategoryDelete();
        categoryDelete.setId(category.getId());

        mockMvc.perform(delete("/category")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(categoryDelete)))
                .andDo(print())
                .andExpect(status().isOk());

        Optional<Category> deletedCateogory = categoryRepository.findById(category.getId());
        assertThat(deletedCateogory.isEmpty()).isTrue();
    }

    @Test
    void 카테고리_삭제_실패_테스트() throws Exception {
        Category category = new Category();
        category.setName("category_test");
        categoryRepository.save(category);


        CategoryDelete categoryDelete = new CategoryDelete();

        mockMvc.perform(delete("/category")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(categoryDelete)))
                .andDo(print())
                .andExpect(status().is4xxClientError());

        Optional<Category> deletedCateogory = categoryRepository.findById(category.getId());
        assertThat(deletedCateogory.isEmpty()).isFalse();
    }

    @Test
    void 카테고리_조회_성공_테스트() throws Exception {
        Category category = new Category();
        category.setName("category_test");
        categoryRepository.save(category);


        CategoryDelete categoryDelete = new CategoryDelete();

        mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(categoryDelete)))
                .andDo(print())
                .andExpect(status().isOk());

        mockMvc.perform(get("/categories/" +category.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(categoryDelete)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}