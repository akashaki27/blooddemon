
package com.blooddemon.catalog_service.controller;

import com.blooddemon.catalog_service.dto.CategoryRequestDto;
import com.blooddemon.catalog_service.dto.CategoryResponseDto;
import com.blooddemon.catalog_service.service.CategoryService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Create category
    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(
            @Valid @RequestBody CategoryRequestDto request) {

        CategoryResponseDto response = categoryService.createCategory(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get all categories
    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories(
            @RequestParam(defaultValue = "false") boolean includeInactive) {

        return ResponseEntity.ok(
                categoryService.getAllCategories(includeInactive));
    }

    // Get category by ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                categoryService.getCategoryById(id));
    }

    // Update category
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDto> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequestDto request) {

        return ResponseEntity.ok(
                categoryService.updateCategory(id, request));
    }

    // Deactivate category
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CategoryResponseDto> deactivateCategory(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                categoryService.deactivateCategory(id));
    }

    // Activate category
    @PatchMapping("/{id}/activate")
    public ResponseEntity<CategoryResponseDto> activateCategory(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                categoryService.activateCategory(id));
    }
}
