package com.awbd.ironplate.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "training_programs")
@Data
public class TrainingProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be at most 100 characters")
    @Column(nullable = false)
    private String name;

    @Size(max = 500, message = "Description must be at most 500 characters")
    private String description;

    @Min(value = 1, message = "Duration must be at least 1 week")
    @Max(value = 52, message = "Duration must be at most 52 weeks")
    private Integer durationWeeks;

    @NotNull(message = "Difficulty level is required")
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "trainingProgram", cascade = CascadeType.ALL)
    private List<Workout> workouts = new ArrayList<>();

    public enum DifficultyLevel {
        BEGINNER, INTERMEDIATE, ADVANCED
    }
}
