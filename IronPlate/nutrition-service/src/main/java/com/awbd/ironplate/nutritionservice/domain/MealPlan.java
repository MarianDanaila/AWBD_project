package com.awbd.ironplate.nutritionservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "meal_plans")
@Data
public class MealPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String name;

    private LocalDate startDate;
    private LocalDate endDate;

    @Min(500) @Max(10000)
    private Integer dailyCalorieTarget;

    private Long userId;
}
