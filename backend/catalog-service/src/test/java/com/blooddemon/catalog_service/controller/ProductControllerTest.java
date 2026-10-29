
package com.blooddemon.catalog_service.controller;

import com.blooddemon.catalog_service.dto.ProductResponseDto;
import com.blooddemon.catalog_service.service.ProductService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void getAllProducts_shouldReturnProducts() throws Exception {
        ProductResponseDto product = new ProductResponseDto(
                1L,
                "Pant",
                "Black Pant",
                new BigDecimal("1001.01"),
                "LKU_101",
                10,
                true);

        when(productService.getAllProducts())
                .thenReturn(List.of(product));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Pant"))
                .andExpect(jsonPath("$[0].sku").value("LKU_101"));
    }

    @Test
    void createProduct_shouldReturn201() throws Exception {
        ProductResponseDto product = new ProductResponseDto(
                1L,
                "Pant",
                "Black Pant",
                new BigDecimal("1001.01"),
                "LKU_101",
                10,
                true);

        when(productService.createProduct(any()))
                .thenReturn(product);

        mockMvc.perform(post("/api/products")
                .contentType(APPLICATION_JSON)
                .content("""
                        {
                          "name": "Pant",
                          "description": "Black Pant",
                          "price": 1001.01,
                          "sku": "LKU_101",
                          "stockQuantity": 10
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Pant"));
    }

    @Test
    void getProductById_shouldReturnProduct() throws Exception {
        ProductResponseDto product = new ProductResponseDto(
                1L,
                "Pant",
                "Black Pant",
                new BigDecimal("1001.01"),
                "LKU_101",
                10,
                true);

        when(productService.getProductById(1L))
                .thenReturn(product);

        mockMvc.perform(get("/api/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.sku").value("LKU_101"));
    }

    @Test
    void createProduct_withInvalidData_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/products")
                .contentType(APPLICATION_JSON)
                .content("""
                        {
                            "name": "",
                            "description": "Test product",
                            "price": 0,
                            "sku": "",
                            "stockQuantity": -1
                        }
                        """))
                .andExpect(status().isBadRequest());
    }
}
