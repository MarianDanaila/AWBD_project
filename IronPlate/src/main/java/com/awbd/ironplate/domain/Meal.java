package com.awbd.ironplate.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "meals")
@Data
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MealType mealType;

    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_plan_id", nullable = false)
    private MealPlan mealPlan;

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL)
    private List<MealFoodItem> mealFoodItems = new ArrayList<>();

    public enum MealType {
        BREAKFAST, LUNCH, DINNER, SNACK
    }
}
