package com.awbd.ironplate.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "workout_exercises")
@Data
public class WorkoutExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id", nullable = false)
    private Workout workout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(name = "num_sets", nullable = false)
    private Integer sets;

    @Column(nullable = false)
    private Integer reps;

    private Double weightKg;

    private String notes;
}
