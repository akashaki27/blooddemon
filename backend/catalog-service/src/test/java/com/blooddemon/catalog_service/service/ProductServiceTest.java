
package com.blooddemon.catalog_service.service;

import com.blooddemon.catalog_service.dto.ProductRequestDto;
import com.blooddemon.catalog_service.dto.ProductResponseDto;
import com.blooddemon.catalog_service.entity.Product;
import com.blooddemon.catalog_service.repository.ProductRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

        @Mock
        private ProductRepository productRepository;

        @InjectMocks
        private ProductService productService;

        @Test
        void createProduct_shouldReturnSavedProduct() {
                ProductRequestDto request = new ProductRequestDto(
                                "Pant",
                                "Black Pant",
                                new BigDecimal("1001.01"),
                                "LKU_101",
                                10);

                Product savedProduct = new Product(
                                "Pant",
                                "Black Pant",
                                new BigDecimal("1001.01"),
                                "LKU_101",
                                10);

                when(productRepository.existsBySku("LKU_101"))
                                .thenReturn(false);

                when(productRepository.save(any(Product.class)))
                                .thenReturn(savedProduct);

                ProductResponseDto response = productService.createProduct(request);

                assertEquals("Pant", response.name());
                assertEquals("LKU_101", response.sku());
                assertEquals(10, response.stockQuantity());
                assertEquals(
                                new BigDecimal("1001.01"),
                                response.price());

                verify(productRepository).existsBySku("LKU_101");
                verify(productRepository).save(any(Product.class));
        }
}
