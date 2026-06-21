package com.awbd.ironplate.nutritionservice.repository;

import com.awbd.ironplate.nutritionservice.domain.FoodItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    Page<FoodItem> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
