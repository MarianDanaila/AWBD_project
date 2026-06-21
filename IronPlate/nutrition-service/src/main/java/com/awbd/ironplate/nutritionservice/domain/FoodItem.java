package com.awbd.ironplate.nutritionservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Table(name = "food_items")
@Data
public class FoodItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private FoodCategory category;

    @NotNull
    @DecimalMin("0.0")
    private Double caloriesPer100g;

    @DecimalMin("0.0")
    private Double proteinPer100g;

    @DecimalMin("0.0")
    private Double carbsPer100g;

    @DecimalMin("0.0")
    private Double fatPer100g;

    public enum FoodCategory { PROTEIN, CARBOHYDRATE, FAT, VEGETABLE, FRUIT, DAIRY, OTHER }
}
