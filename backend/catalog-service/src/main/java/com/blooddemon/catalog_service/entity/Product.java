package com.blooddemon.catalog_service.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products", uniqueConstraints = {
        @UniqueConstraint(name = "uk_product_sku", columnNames = "sku")
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, length = 50)
    private String sku;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(nullable = false)
    private boolean active = true;

    protected Product() {
        // Required by JPA
    }

    public Product(
            String name,
            String description,
            BigDecimal price,
            String sku,
            Integer stockQuantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.sku = sku;
        this.stockQuantity = stockQuantity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getSku() {
        return sku;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public boolean isActive() {
        return active;
    }

    public void updateDetails(
            String name,
            String description,
            BigDecimal price,
            String sku,
            Integer stockQuantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.sku = sku;
        this.stockQuantity = stockQuantity;
    }
}
