 @Query("""
            SELECT p FROM Product p
            WHERE (:name IS NULL OR
                   LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:minPrice IS NULL OR p.price >= :minPrice)
              AND (:maxPrice IS NULL OR p.price <= :maxPrice)
            """)
    Page<Product> searchProducts(
            @Param("name") String name,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            Pageable pageable);
}