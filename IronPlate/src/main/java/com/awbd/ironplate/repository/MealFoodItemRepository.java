package com.awbd.ironplate.repository;

import com.awbd.ironplate.domain.MealFoodItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealFoodItemRepository extends JpaRepository<MealFoodItem, Long> {

    List<MealFoodItem> findByMealId(Long mealId);

    List<MealFoodItem> findByFoodItemId(Long foodItemId);

    void deleteByMealId(Long mealId);
}
