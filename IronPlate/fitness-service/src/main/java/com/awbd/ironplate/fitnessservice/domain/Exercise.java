package com.awbd.ironplate.fitnessservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Table(name = "exercises")
@Data
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MuscleGroup muscleGroup;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ExerciseType exerciseType;

    public enum MuscleGroup { CHEST, BACK, LEGS, SHOULDERS, ARMS, CORE, FULL_BODY }
    public enum ExerciseType { STRENGTH, CARDIO, MOBILITY, PLYOMETRIC }
}
