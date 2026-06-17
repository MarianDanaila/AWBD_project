package com.awbd.ironplate.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exercises")
@Data
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    @Column(nullable = false)
    private String name;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    @NotNull(message = "Muscle group is required")
    @Enumerated(EnumType.STRING)
    private MuscleGroup muscleGroup;

    @NotNull(message = "Exercise type is required")
    @Enumerated(EnumType.STRING)
    private ExerciseType exerciseType;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL)
    private List<WorkoutExercise> workoutExercises = new ArrayList<>();

    public enum MuscleGroup {
        CHEST, BACK, LEGS, SHOULDERS, ARMS, CORE, FULL_BODY
    }

    public enum ExerciseType {
        STRENGTH, CARDIO, MOBILITY, PLYOMETRIC
    }
}
