package com.awbd.ironplate.domain;

import jakarta.persistence.*;
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

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private MuscleGroup muscleGroup;

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
