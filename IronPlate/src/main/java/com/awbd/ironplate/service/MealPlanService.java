package com.awbd.ironplate.service;

import com.awbd.ironplate.domain.MealPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MealPlanService {

    MealPlan save(MealPlan mealPlan);

    MealPlan findById(Long id);

    Page<MealPlan> findAll(Pageable pageable);

    Page<MealPlan> findByUserId(Long userId, Pageable pageable);

    void deleteById(Long id);
}
