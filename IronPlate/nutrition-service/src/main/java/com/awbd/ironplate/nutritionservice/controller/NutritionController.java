package com.awbd.ironplate.nutritionservice.controller;

import com.awbd.ironplate.nutritionservice.domain.FoodItem;
import com.awbd.ironplate.nutritionservice.domain.MealPlan;
import com.awbd.ironplate.nutritionservice.service.NutritionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class NutritionController {

    private final NutritionService nutritionService;

    @GetMapping("/food-items")
    public Page<FoodItem> listFoodItems(Pageable pageable) {
        return nutritionService.findAllFoodItems(pageable);
    }

    @GetMapping("/food-items/{id}")
    public ResponseEntity<FoodItem> getFoodItem(@PathVariable Long id) {
        return ResponseEntity.ok(nutritionService.findFoodItemById(id));
    }

    @PostMapping("/food-items")
    public ResponseEntity<FoodItem> createFoodItem(@Valid @RequestBody FoodItem foodItem) {
        return ResponseEntity.ok(nutritionService.saveFoodItem(foodItem));
    }

    @PutMapping("/food-items/{id}")
    public ResponseEntity<FoodItem> updateFoodItem(@PathVariable Long id, @Valid @RequestBody FoodItem foodItem) {
        foodItem.setId(id);
        return ResponseEntity.ok(nutritionService.saveFoodItem(foodItem));
    }

    @DeleteMapping("/food-items/{id}")
    public ResponseEntity<Void> deleteFoodItem(@PathVariable Long id) {
        nutritionService.deleteFoodItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/meal-plans")
    public Page<MealPlan> listMealPlans(
            @RequestParam(required = false) Long userId, Pageable pageable) {
        return userId != null
                ? nutritionService.findMealPlansByUser(userId, pageable)
                : nutritionService.findAllMealPlans(pageable);
    }

    @GetMapping("/meal-plans/{id}")
    public ResponseEntity<MealPlan> getMealPlan(@PathVariable Long id) {
        return ResponseEntity.ok(nutritionService.findMealPlanById(id));
    }

    @PostMapping("/meal-plans")
    public ResponseEntity<MealPlan> createMealPlan(@Valid @RequestBody MealPlan mealPlan) {
        return ResponseEntity.ok(nutritionService.saveMealPlan(mealPlan));
    }

    @PutMapping("/meal-plans/{id}")
    public ResponseEntity<MealPlan> updateMealPlan(@PathVariable Long id, @Valid @RequestBody MealPlan mealPlan) {
        mealPlan.setId(id);
        return ResponseEntity.ok(nutritionService.saveMealPlan(mealPlan));
    }

    @DeleteMapping("/meal-plans/{id}")
    public ResponseEntity<Void> deleteMealPlan(@PathVariable Long id) {
        nutritionService.deleteMealPlan(id);
        return ResponseEntity.noContent().build();
    }
}
