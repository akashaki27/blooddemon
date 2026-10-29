package com.blooddemon.catalog_service.dto;

import java.math.BigDecimal;

public record ProductResponseDto(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String sku,
        Integer stockQuantity,
        boolean active) {
}
