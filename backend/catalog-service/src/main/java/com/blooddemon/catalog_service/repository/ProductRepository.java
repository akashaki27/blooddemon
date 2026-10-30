
package com.blooddemon.catalog_service.repository;

import com.blooddemon.catalog_service.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

  Optional<Product> findBySku(String sku);

  boolean existsBySku(String sku);

  @Query("""
      SELECT p FROM Product p
      WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))
        AND (:minPrice IS NULL OR p.price >= :minPrice)
        AND (:maxPrice IS NULL OR p.price <= :maxPrice)
        AND (
            :inStock IS NULL
            OR (:inStock = true AND p.stockQuantity > 0)
            OR (:inStock = false AND p.stockQuantity = 0)
        )
      """)
  Page<Product> searchByFilters(
      @Param("name") String name,
      @Param("minPrice") BigDecimal minPrice,
      @Param("maxPrice") BigDecimal maxPrice,
      @Param("inStock") Boolean inStock,
      Pageable pageable);

  @Query("""
      SELECT p FROM Product p
      WHERE (:minPrice IS NULL OR p.price >= :minPrice)
        AND (:maxPrice IS NULL OR p.price <= :maxPrice)
        AND (
            :inStock IS NULL
            OR (:inStock = true AND p.stockQuantity > 0)
            OR (:inStock = false AND p.stockQuantity = 0)
        )
      """)
  Page<Product> findByFilters(
      @Param("minPrice") BigDecimal minPrice,
      @Param("maxPrice") BigDecimal maxPrice,
      @Param("inStock") Boolean inStock,
      Pageable pageable);
}
