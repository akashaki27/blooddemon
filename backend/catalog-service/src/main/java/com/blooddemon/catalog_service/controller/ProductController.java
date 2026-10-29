
package com.blooddemon.catalog_service.controller;

import com.blooddemon.catalog_service.dto.ProductRequestDto;
import com.blooddemon.catalog_service.dto.ProductResponseDto;
import com.blooddemon.catalog_service.service.ProductService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    // Dependency injection for product operations.
    private final ProductService productService;

    // Constructor-based injection to keep the controller immutable and testable.
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Creates a new product with validation on the request payload.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponseDto createProduct(
            @Valid @RequestBody ProductRequestDto request) {
        return productService.createProduct(request);
    }

    /**
     * Returns products with paging and sorting.
     * Validates pagination and sort parameters before querying.
     */
    @GetMapping
    public Page<ProductResponseDto> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        if (page < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Page must be 0 or greater");
        }

        if (size < 1 || size > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Size must be between 1 and 100");
        }

        if (!java.util.Set.of(
                "id", "name", "price", "sku", "stockQuantity")
                .contains(sortBy)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid sort field");
        }

        if (!direction.equalsIgnoreCase("asc")
                && !direction.equalsIgnoreCase("desc")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Direction must be asc or desc");
        }

        return productService.getProducts(page, size, sortBy, direction);
    }

    /**
     * Fetches one product by its identifier.
     * Returns the product details for the requested ID.
     */
    @GetMapping("/{id}")
    public ProductResponseDto getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    /**
     * Updates an existing product by ID.
     * Validates the incoming request before persisting changes.
     */
    @PutMapping("/{id}")
    public ProductResponseDto updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDto request) {
        return productService.updateProduct(id, request);
    }

    // Deletes a product by id and returns no content.
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}
