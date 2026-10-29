package com.blooddemon.catalog_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequestDto(

                @NotBlank(message = "Product name is required") @Size(max = 150, message = "Product name cannot exceed 150 characters") String name,

                @Size(max = 1000, message = "Description cannot exceed 1000 characters") String description,

                @NotNull(message = "Price is required") @DecimalMin(value = "0.01", message = "Price must be greater than zero") BigDecimal price,

                @NotBlank(message = "SKU is required") @Size(max = 50, message = "SKU cannot exceed 50 characters") String sku,

                @NotNull(message = "Stock quantity is required") @Min(value = 0, message = "Stock quantity cannot be negative") Integer stockQuantity

) {
}