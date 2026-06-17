package com.awbd.ironplate.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "meal_food_items")
@Data
public class MealFoodItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_item_id", nullable = false)
    private FoodItem foodItem;

    @Column(nullable = false)
    private Double quantityGrams;
}
