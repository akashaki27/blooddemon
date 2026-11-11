
package com.blooddemon.catalog_service.controller;

import com.blooddemon.catalog_service.dto.CategoryRequestDto;
import com.blooddemon.catalog_service.dto.CategoryResponseDto;
import com.blooddemon.catalog_service.service.CategoryService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @Test
    void shouldCreateCategory() throws Exception {

        when(categoryService.createCategory(any(CategoryRequestDto.class)))
                .thenReturn(new CategoryResponseDto(
                        1L, "Men", "Men's clothing", true));

        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "Men",
                            "description": "Men's clothing"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Men"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void shouldReturnAllActiveCategories() throws Exception {

        when(categoryService.getAllCategories(false))
                .thenReturn(List.of(
                        new CategoryResponseDto(
                                1L, "Men", "Men's clothing", true)));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Men"))
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    void shouldReturnCategoryById() throws Exception {

        when(categoryService.getCategoryById(1L))
                .thenReturn(new CategoryResponseDto(
                        1L, "Men", "Men's clothing", true));

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Men"));
    }

    @Test
    void shouldReturnBadRequestWhenCategoryNameIsBlank() throws Exception {

        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": " ",
                            "description": "Men's clothing"
                        }
                        """))
                .andExpect(status().isBadRequest());

        verify(categoryService, never())
                .createCategory(any(CategoryRequestDto.class));
    }

    @Test
    void shouldUpdateCategory() throws Exception {

        when(categoryService.updateCategory(
                eq(1L), any(CategoryRequestDto.class)))
                .thenReturn(new CategoryResponseDto(
                        1L, "Updated Men", "Updated description", true));

        mockMvc.perform(put("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "Updated Men",
                            "description": "Updated description"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Men"));
    }

    @Test
    void shouldDeactivateCategory() throws Exception {

        when(categoryService.deactivateCategory(1L))
                .thenReturn(new CategoryResponseDto(
                        1L, "Men", "Men's clothing", false));

        mockMvc.perform(patch("/api/categories/1/deactivate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false));
    }

    @Test
    void shouldActivateCategory() throws Exception {

        when(categoryService.activateCategory(1L))
                .thenReturn(new CategoryResponseDto(
                        1L, "Men", "Men's clothing", true));

        mockMvc.perform(patch("/api/categories/1/activate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(true));
    }
}
