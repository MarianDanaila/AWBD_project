package com.awbd.ironplate.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Calories are required")
    @DecimalMin(value = "0.0", message = "Calories cannot be negative")
    private Double caloriesPer100g;

    @DecimalMin(value = "0.0", message = "Protein cannot be negative")
    private Double proteinPer100g;

    @DecimalMin(value = "0.0", message = "Carbs cannot be negative")
    private Double carbsPer100g;

    @DecimalMin(value = "0.0", message = "Fat cannot be negative")
    private Double fatPer100g;

    @NotNull(message = "Category is required")
    @Enumerated(EnumType.STRING)
    private FoodCategory category;

    @OneToMany(mappedBy = "foodItem", cascade = CascadeType.ALL)
    private List<MealFoodItem> mealFoodItems = new ArrayList<>();

    public enum FoodCategory {
        PROTEIN, CARBOHYDRATE, FAT, VEGETABLE, FRUIT, DAIRY, OTHER
    }
}
