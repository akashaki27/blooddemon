
package com.blooddemon.catalog_service.service;

import com.blooddemon.catalog_service.dto.CategoryRequestDto;
import com.blooddemon.catalog_service.dto.CategoryResponseDto;
import com.blooddemon.catalog_service.entity.Category;
import com.blooddemon.catalog_service.repository.CategoryRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Create category
    public CategoryResponseDto createCategory(CategoryRequestDto request) {

        if (categoryRepository.existsByNameIgnoreCase(request.name())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Category already exists with name: " + request.name());
        }

        Category category = new Category(
                request.name(),
                request.description());

        Category savedCategory = categoryRepository.save(category);

        return toResponseDto(savedCategory);
    }

    // Get all categories
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategories(boolean includeInactive) {

        List<Category> categories = includeInactive
                ? categoryRepository.findAll()
                : categoryRepository.findAllByActiveTrue();

        return categories.stream()
                .map(this::toResponseDto)
                .toList();
    }

    // Get category by ID
    @Transactional(readOnly = true)
    public CategoryResponseDto getCategoryById(Long id) {

        Category category = findCategoryById(id);

        return toResponseDto(category);
    }

    // Update category
    public CategoryResponseDto updateCategory(
            Long id,
            CategoryRequestDto request) {

        Category category = findCategoryById(id);

        if (!category.getName().equalsIgnoreCase(request.name())
                && categoryRepository.existsByNameIgnoreCase(request.name())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Category already exists with name: " + request.name());
        }

        category.updateDetails(
                request.name(),
                request.description());

        Category updatedCategory = categoryRepository.save(category);

        return toResponseDto(updatedCategory);
    }

    // Soft delete category
    public CategoryResponseDto deactivateCategory(Long id) {

        Category category = findCategoryById(id);

        if (!category.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Category is already inactive");
        }

        category.deactivate();

        return toResponseDto(categoryRepository.save(category));
    }

    // Reactivate category
    public CategoryResponseDto activateCategory(Long id) {

        Category category = findCategoryById(id);

        if (category.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Category is already active");
        }

        category.activate();

        return toResponseDto(categoryRepository.save(category));
    }

    // Find category or throw 404
    private Category findCategoryById(Long id) {

        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Category not found with ID: " + id));
    }

    // Convert Entity to Response DTO
    private CategoryResponseDto toResponseDto(Category category) {

        return new CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.isActive());
    }
}
