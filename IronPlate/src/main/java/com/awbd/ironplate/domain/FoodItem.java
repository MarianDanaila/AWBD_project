package com.awbd.ironplate.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "food_items")
@Data
public class FoodItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Double caloriesPer100g;
    private Double proteinPer100g;
    private Double carbsPer100g;
    private Double fatPer100g;

    @Enumerated(EnumType.STRING)
    private FoodCategory category;

    @OneToMany(mappedBy = "foodItem", cascade = CascadeType.ALL)
    private List<MealFoodItem> mealFoodItems = new ArrayList<>();

    public enum FoodCategory {
        PROTEIN, CARBOHYDRATE, FAT, VEGETABLE, FRUIT, DAIRY, OTHER
    }
}
