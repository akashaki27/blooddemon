
package com.blooddemon.catalog_service.service;

import com.blooddemon.catalog_service.dto.CategoryRequestDto;
import com.blooddemon.catalog_service.dto.CategoryResponseDto;
import com.blooddemon.catalog_service.entity.Category;
import com.blooddemon.catalog_service.repository.CategoryRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void shouldCreateCategorySuccessfully() {
        CategoryRequestDto request = new CategoryRequestDto("Men's Clothing", "Clothing for men");

        Category savedCategory = new Category("Men's Clothing", "Clothing for men");

        when(categoryRepository.existsByNameIgnoreCase(request.name()))
                .thenReturn(false);

        when(categoryRepository.save(any(Category.class)))
                .thenReturn(savedCategory);

        CategoryResponseDto response = categoryService.createCategory(request);

        assertEquals("Men's Clothing", response.name());
        assertEquals("Clothing for men", response.description());
        assertTrue(response.active());

        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void shouldThrowExceptionWhenCategoryAlreadyExists() {
        CategoryRequestDto request = new CategoryRequestDto("Men's Clothing", "Clothing for men");

        when(categoryRepository.existsByNameIgnoreCase(request.name()))
                .thenReturn(true);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> categoryService.createCategory(request));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());

        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void shouldReturnOnlyActiveCategoriesByDefault() {
        List<Category> categories = List.of(
                new Category("Men", "Men's clothing"),
                new Category("Women", "Women's clothing"));

        when(categoryRepository.findAllByActiveTrue())
                .thenReturn(categories);

        List<CategoryResponseDto> response = categoryService.getAllCategories(false);

        assertEquals(2, response.size());

        verify(categoryRepository).findAllByActiveTrue();
        verify(categoryRepository, never()).findAll();
    }

    @Test
    void shouldReturnAllCategoriesWhenIncludeInactiveIsTrue() {
        when(categoryRepository.findAll()).thenReturn(List.of(
                new Category("Men", "Men's clothing"),
                new Category("Women", "Women's clothing")));

        List<CategoryResponseDto> response = categoryService.getAllCategories(true);

        assertEquals(2, response.size());

        verify(categoryRepository).findAll();
        verify(categoryRepository, never()).findAllByActiveTrue();
    }

    @Test
    void shouldThrowExceptionWhenCategoryDoesNotExist() {
        when(categoryRepository.findById(100L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> categoryService.getCategoryById(100L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void shouldDeactivateCategorySuccessfully() {
        Category category = new Category("Men", "Men's clothing");

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(categoryRepository.save(category))
                .thenReturn(category);

        CategoryResponseDto response = categoryService.deactivateCategory(1L);

        assertFalse(response.active());
        verify(categoryRepository).save(category);
    }

    @Test
    void shouldThrowExceptionWhenCategoryIsAlreadyInactive() {
        Category category = new Category("Men", "Men's clothing");
        category.deactivate();

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> categoryService.deactivateCategory(1L));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());

        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void shouldActivateCategorySuccessfully() {
        Category category = new Category("Men", "Men's clothing");
        category.deactivate();

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(categoryRepository.save(category))
                .thenReturn(category);

        CategoryResponseDto response = categoryService.activateCategory(1L);

        assertTrue(response.active());
        verify(categoryRepository).save(category);
    }
}
