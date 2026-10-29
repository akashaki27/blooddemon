package com.blooddemon.catalog_service.service;

import com.blooddemon.catalog_service.dto.ProductRequestDto;
import com.blooddemon.catalog_service.dto.ProductResponseDto;
import com.blooddemon.catalog_service.entity.Product;
import com.blooddemon.catalog_service.repository.ProductRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Create a product unless its SKU is already assigned to another product.
    public ProductResponseDto createProduct(ProductRequestDto request) {
        if (productRepository.existsBySku(request.sku())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A product with this SKU already exists");
        }

        Product product = new Product(
                request.name(),
                request.description(),
                request.price(),
                request.sku(),
                request.stockQuantity());

        // Persist the new entity before mapping it to the API response.
        Product savedProduct = productRepository.save(product);

        return toResponseDto(savedProduct);
    }

    // Return all products as response DTOs without exposing entity objects.
    @Transactional(readOnly = true)
    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    // Look up one product and report a 404 when it does not exist.
    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found with ID: " + id));

        return toResponseDto(product);
    }

    public ProductResponseDto updateProduct(
            Long id,
            ProductRequestDto request) {

        // An update cannot proceed unless the target product exists.
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found with ID: " + id));

        boolean skuChanged = !product.getSku().equals(request.sku());

        // Check uniqueness only when the request changes the product's SKU.
        if (skuChanged && productRepository.existsBySku(request.sku())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A product with this SKU already exists");
        }

        product.updateDetails(
                request.name(),
                request.description(),
                request.price(),
                request.sku(),
                request.stockQuantity());

        // Save the updated entity and return its DTO representation.
        Product savedProduct = productRepository.save(product);

        return toResponseDto(savedProduct);
    }

    // Return a page of products, sorted and paginated according to the request
    // parameters.
    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getProducts(
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort sort = "desc".equalsIgnoreCase(direction)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return productRepository.findAll(pageable)
                .map(this::toResponseDto);
    }

    // Delete the requested product, returning 404 if it cannot be found.
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found with ID: " + id));

        productRepository.delete(product);
    }

    // Keep API responses decoupled from the persistence entity.
    private ProductResponseDto toResponseDto(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getSku(),
                product.getStockQuantity(),
                product.isActive());
    }
}
