package com.awbd.ironplate.repository;

import com.awbd.ironplate.domain.FoodItem;
import com.awbd.ironplate.domain.FoodItem.FoodCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

    Page<FoodItem> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<FoodItem> findByCategory(FoodCategory category);
}
