package com.awbd.ironplate.repository;

import com.awbd.ironplate.domain.Meal;
import com.awbd.ironplate.domain.Meal.MealType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {

    List<Meal> findByMealPlanId(Long mealPlanId);

    List<Meal> findByMealPlanIdAndMealType(Long mealPlanId, MealType mealType);
}
