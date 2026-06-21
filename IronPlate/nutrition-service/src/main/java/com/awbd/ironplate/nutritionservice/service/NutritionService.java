package com.awbd.ironplate.nutritionservice.service;

import com.awbd.ironplate.nutritionservice.client.UserClient;
import com.awbd.ironplate.nutritionservice.client.UserDto;
import com.awbd.ironplate.nutritionservice.domain.FoodItem;
import com.awbd.ironplate.nutritionservice.domain.MealPlan;
import com.awbd.ironplate.nutritionservice.repository.FoodItemRepository;
import com.awbd.ironplate.nutritionservice.repository.MealPlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NutritionService {

    private final FoodItemRepository foodItemRepository;
    private final MealPlanRepository mealPlanRepository;
    private final UserClient userClient;

    public Page<FoodItem> findAllFoodItems(Pageable pageable) {
        return foodItemRepository.findAll(pageable);
    }

    @Cacheable(value = "food-items", key = "#id")
    public FoodItem findFoodItemById(Long id) {
        return foodItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("FoodItem not found: " + id));
    }

    @CacheEvict(value = "food-items", key = "#result.id")
    public FoodItem saveFoodItem(FoodItem foodItem) {
        log.info("Saving food item: {}", foodItem.getName());
        return foodItemRepository.save(foodItem);
    }

    @CacheEvict(value = "food-items", key = "#id")
    public void deleteFoodItem(Long id) {
        log.info("Deleting food item: {}", id);
        foodItemRepository.deleteById(id);
    }

    public Page<MealPlan> findAllMealPlans(Pageable pageable) {
        return mealPlanRepository.findAll(pageable);
    }

    public Page<MealPlan> findMealPlansByUser(Long userId, Pageable pageable) {
        return mealPlanRepository.findByUserId(userId, pageable);
    }

    public MealPlan findMealPlanById(Long id) {
        return mealPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MealPlan not found: " + id));
    }

    public MealPlan saveMealPlan(MealPlan mealPlan) {
        if (mealPlan.getUserId() != null) {
            UserDto user = userClient.getUserById(mealPlan.getUserId());
            if (user == null) {
                log.warn("User {} not found or user-service unavailable — saving meal plan anyway", mealPlan.getUserId());
            } else {
                log.info("Validated user {} for meal plan", user.getUsername());
            }
        }
        log.info("Saving meal plan: {}", mealPlan.getName());
        return mealPlanRepository.save(mealPlan);
    }

    public void deleteMealPlan(Long id) {
        log.info("Deleting meal plan: {}", id);
        mealPlanRepository.deleteById(id);
    }
}
