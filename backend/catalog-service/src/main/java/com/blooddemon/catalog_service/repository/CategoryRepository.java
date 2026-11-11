package com.blooddemon.catalog_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.blooddemon.catalog_service.entity.Category;

import java.util.Optional;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Find a category by its name, ignoring case
    Optional<Category> findByNameIgnoreCase(String name);

    // Check if a category with the given name exists, ignoring case
    boolean existsByNameIgnoreCase(String name);

    List<Category> findAllByActiveTrue();

}
