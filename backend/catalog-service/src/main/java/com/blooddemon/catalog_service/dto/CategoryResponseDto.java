package com.blooddemon.catalog_service.dto;

public record CategoryResponseDto(

        Long id,
        String name,
        String description,
        boolean active

) {
}
