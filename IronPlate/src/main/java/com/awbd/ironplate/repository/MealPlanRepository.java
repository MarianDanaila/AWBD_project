package com.awbd.ironplate.repository;

import com.awbd.ironplate.domain.MealPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealPlanRepository extends JpaRepository<MealPlan, Long> {

    Page<MealPlan> findByUserId(Long userId, Pageable pageable);

    Page<MealPlan> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
